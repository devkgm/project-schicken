package com.groups.schicken.chatting;

import com.groups.schicken.Employee.EmployeeDAO;
import com.groups.schicken.Employee.EmployeeProfileVO;
import com.groups.schicken.Employee.EmployeeVO;
import com.groups.schicken.common.util.DateManager;
import com.groups.schicken.notification.Noticer;
import com.groups.schicken.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatDAO chatDAO;
    private final EmployeeDAO employeeDAO;
    private final SimpMessagingTemplate template;

    /*

        채팅방 관련

    */

    /**
     * 1:1 채팅방의 정보를 가져온다
     *
     * @param reqId    채팅방 정보를 가져오려는 사람의 아이디
     * @param targetId 채팅하려는 대상의 아이디
     */
    public ChatroomVO getOneChatrooms(String reqId, String targetId) {
        String chatroomId = makeOneChatroomId(List.of(reqId, targetId));
        return chatDAO.getChatroomByChatroomId(chatroomId, reqId);
    }

    /**
     * 두 사람의 아이디를 받아 1:1 채팅방 생성
     * 채팅방의 기본 이름은 서로 상대방의 이름이 된다
     * 채팅방 생성후 자동으로 채팅방에 입장
     */
    @Transactional
    public ChatroomVO createOneChatroom(String id1, String id2) {
        List<String> ids = List.of(id1, id2);
        String chatroomId = makeOneChatroomId(ids);

        ChatroomVO created = new ChatroomVO();
        created.setId(chatroomId);
        created.setType(ChatroomType.One);
        int result = chatDAO.createOneChatroom(created);
        if (result != 1) {
            throw new RuntimeException("채팅방 생성 실패");
        }

        List<EmployeeVO> employees = employeeDAO.getNamesByIds(ids);
        for (EmployeeVO employee : employees) {
            for (String id : ids) {
                if (id.equals(employee.getId())) continue;
                employee.setId(id);
                break;
            }
            created.setName(employee.getName());

            if (!joinChatroom(employee.getId(), created)) {
                throw new RuntimeException("생성된 채팅방 입장 실패");
            }
        }

        return created;
    }

    /**
     * 채팅방에 입장한다
     *
     * @param id       입장하려는 사람의 아이디
     * @param chatroom 입장하려는 채팅방의 VO
     * @return DB에 insert가 성공시 true 그렇지 않으면 false
     */
    public Boolean joinChatroom(String id, ChatroomVO chatroom) {
        chatroom.setJoinDate(DateManager.getTodayDateTime("yyyyMMddHHmmssSSS"));
        int result = chatDAO.joinChatroom(id, chatroom);
        return result == 1;
    }

    /**
     * 채팅방에 입장한다
     * 채팅방의 기본 이름을 생성하여 입장한다
     *
     * @param id         입장하려는 사람의 아이디
     * @param chatroomId 입장하려는 채팅방의 아이디
     * @return DB에 insert가 성공시 true 그렇지 않으면 false
     */
    public Boolean joinChatroom(String id, String chatroomId) {
        ChatroomVO chatroom = new ChatroomVO();
        chatroom.setId(chatroomId);
        chatroom.setName(getChatroomName(chatroomId));

        return joinChatroom(id, chatroom);
    }

    @Transactional
    public int joinChatroom(String chatroomId, String[] members) {
        String joinDate = DateManager.getTodayDateTime("yyyyMMddHHmmssSSS");
        String name = getChatroomName(chatroomId);
        int result = chatDAO.insertMember(chatroomId, name, joinDate, members);

        return result;
    }


    /**
     * employeeId로 해당 employee가 들어가 있는 채팅방 리스트를 가져온다
     */
    public List<ChatroomVO> getChatroomList(String employeeId) {
        List<ChatroomVO> chatroomList = chatDAO.getChatroomList(employeeId);

        List<ChatMessage> lastChatByChatrooms = chatDAO.getLastChatData(chatroomList.stream().map(ChatroomVO::getId).toList());

        for (ChatroomVO chatroomVO : chatroomList) {
            chatroomVO.setLastMessage(
                    lastChatByChatrooms.stream()
                            .filter(e->chatroomVO.getId().equals(e.getChatroomId()))
                            .findAny()
                            .orElse(ChatMessage.EMPTY_MESSAGE)
            );
        }

        chatroomList.sort(
                Comparator
                        .comparing(chatroomVO -> ((ChatroomVO)chatroomVO).getLastMessage().getSendDate())
                        .reversed()
        );

        return chatroomList;
    }

    /**
     * 채팅방에 참여할 멤버 정보를 받아 채팅방을 생성
     * @return 만들어진 채팅방으로 들어갈 수 있는 정보
     */
    @Transactional
    public ChatroomVO createManyChatroom(String empId, String[] members) {
        ChatroomVO created = new ChatroomVO();
        created.setType(ChatroomType.Many);
        created.setJoinDate(DateManager.getTodayDateTime("yyyyMMddhhmmssSSS"));

        List<EmployeeVO> names = employeeDAO.getNamesByIds(List.of(empId));
        if(names.isEmpty()){
            throw new RuntimeException("접속한 아이디가 유효하지 않습니다.");
        }

        created.setName(names.get(0).getName() + "님의 채팅방");
        int result = chatDAO.createChatroom(created);

        if(result < 1){
            throw new RuntimeException("채팅방 생성실패");
        }

        boolean joinResult =true;
        joinResult &= joinChatroom(empId, created);

        for (String member : members) {
            joinResult &= joinChatroom(member, created);
        }

        if(!joinResult){
            throw new RuntimeException("채팅방 입장 실패");
        }

        return created;
    }

    /**
     * 1 : 1 채팅방 아이디를 두 사람의 id를 합하여 생성
     * @param list 채팅하려는 두 사람의 id를 담은 List
     * @return 두 id를 정렬하여 합친 문자열
     */
    private String makeOneChatroomId(List<String> list) {
        return list.stream().sorted().collect(Collectors.joining());
    }

    /**
     * 채팅방의 이름을 채팅방의 id를 이용하여 가져옴
     */
    private String getChatroomName(String chatroomId) {
        return chatDAO.getChatroomName(chatroomId);
    }


    /*

        채팅 관련

    */

    /**
     * 채팅방 아이디, 채팅 보낸 사람 아이디, 채팅 내용을 받아
     * 채팅의 아이디와 보낸시간을 생성하고 Chatmessage 객체로 리턴
     */
    public ChatMessage makeMessage(String chatroomId, String senderId, String content) {
        String sendDate = DateManager.getTodayDateTime("yyyyMMddHHmmssSSS");
        String id = createChatId(chatroomId, senderId, sendDate);

        content = content.replaceAll("<","&lt;").replaceAll("\n", "<br>");

        return ChatMessage.of(id, chatroomId, senderId, sendDate, content);
    }

    private String createChatId(String chatroomId, String senderId, String sendDate){
        return (new BigInteger(chatroomId + senderId + sendDate)).toString(16);
    }

    /**
     * 메세지를 DB에 Insert 후 해당 채팅을 읽음으로 표시
     */
    @Transactional
    public Boolean insertMessage(ChatMessage message) {
        int result = chatDAO.insertChat(message);

        if (result == 1){
            result += chatDAO.updateLastRead(message.getSendDate(), message.getChatroomId(), message.getSenderId());
        }

        return result >= 1;
    }

    /**
     * 채팅방을 처음 열 때 데이터를 가져옴
     */
    public ChattingVO getChattingDataFirst(String id, String chatroomId) {
        return getChattingDataFirst(id, chatroomId, null);
    }

    /**
     * 채팅방을 처음 열 때 데이터를 가져옴
     */
    @Transactional
    public ChattingVO getChattingDataFirst(String employeeId, String chatroomId, String page){
        ChattingVO chattingData = chatDAO.getChatroomData(employeeId, chatroomId);

        if(chattingData == null){
            return null;
        }

        if(page == null) page = "0";

        String joinDate = chatDAO.getJoinDate(employeeId, chatroomId);

        List<ChatMessage> chatMessageData = chatDAO.getChatMessageDataFirst(chatroomId, chattingData.getLastReadTime(), page, joinDate);
        Collections.reverse(chatMessageData);

        chattingData.setChatMessages(chatMessageData);

        if(!chattingData.isLastReaded()){
            chatDAO.updateLastRead(chattingData.getLastMessage().getSendDate(), chatroomId, employeeId);
        }

        return chattingData;
    }

    /**
     * 채팅방에서 다음 채팅을 불러와줌
     * @param employeeId 접속한 아이디
     * @param chatroomId 채팅방 아이디
     * @param from 마지막 채팅의 번호
     * @param direction 아래방향인지 윗방향인지
     * @return
     */
    public List<ChatMessage> getChattingDataNext(String employeeId, String chatroomId, String from, String direction){
        String joinDate = chatDAO.getJoinDate(employeeId, chatroomId);
        List<ChatMessage> chatMessageData = chatDAO.getChatMessageData(chatroomId, from, direction, joinDate);

        if(!chatMessageData.isEmpty() && direction.equals("down")){
            chatDAO.updateLastRead(chatMessageData.get(chatMessageData.size()-1).getSendDate(), chatroomId, employeeId);
        }

        return chatMessageData;
    }

    /**
     * 채팅을 읽음 처리함
     */
    public Boolean readChatting(String employeeId, String chatroomId, String chatId) {
        int result = chatDAO.updateLastReadById(employeeId, chatroomId, chatId);
        return result == 1;
    }

    public Boolean updateTitle(EmployeeVO employee, ChatroomVO chatroom) {
        int result = chatDAO.updateTitle(employee.getId(), chatroom.getId(), chatroom.getName());

        return result == 1;
    }


    public List<EmployeeProfileVO> getChatroomMemberData(String chatroomId) {
        return chatDAO.getMembersByChatroomId(chatroomId);
    }

    @Transactional
    public void insertJoinNotice(EmployeeVO sender, String chatroomId, String[] members) {
        List<EmployeeVO> employees = employeeDAO.getNamesByIds(Arrays.asList(members));
        String sendDate = null;

        for (EmployeeVO employee : employees) {
            sendDate = DateManager.getTodayDateTime("yyyyMMddHHmmssSSS");

            ChatMessage noticeChat = ChatMessage.builder()
                    .id(createChatId(chatroomId, employee.getId(), sendDate))
                    .chatroomId(chatroomId)
                    .senderId(sender.getId())
                    .type(ChattingType.Join)
                    .sendDate(sendDate)
                    .content(sender.getName() + "님께서 " + employee.getName() + "님을 초대했습니다.")
                    .build();

            chatDAO.insertChat(noticeChat);
            template.convertAndSend("/sub/chat/" + chatroomId, noticeChat);
            template.convertAndSend("/sub/chat/" + employee.getId(), noticeChat);
        }
        if(sendDate != null) chatDAO.updateLastRead(sendDate, chatroomId, sender.getId());
    }

    @Transactional
    public boolean outChatroom(String id, String chatroomId) {
        int result = chatDAO.outChatroom(id, chatroomId);

        if(result != 1) throw new RuntimeException();

        return true;
    }

    public void insertOutMessage(EmployeeVO employee, String chatroomId) {
        String sendDate = DateManager.getTodayDateTime("yyyyMMddHHmmssSSS");

        ChatMessage noticeChat = ChatMessage.builder()
                .id(createChatId(chatroomId, employee.getId(), sendDate))
                .chatroomId(chatroomId)
                .senderId(employee.getId())
                .type(ChattingType.Out)
                .sendDate(sendDate)
                .content(employee.getName() + "님께서 채팅방을 나가셨습니다.")
                .build();

        chatDAO.insertChat(noticeChat);
        template.convertAndSend("/sub/chat/" + chatroomId, noticeChat);
    }
}
