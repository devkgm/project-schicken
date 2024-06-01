package com.groups.schicken.noteMessage;

import com.groups.schicken.Employee.EmployeeVO;
import com.groups.schicken.common.vo.FileVO;
import com.groups.schicken.common.util.FileManager;
import com.groups.schicken.common.vo.Pager;
import com.groups.schicken.notification.Noticer;
import com.groups.schicken.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteMessageService {
    private final NoteMessageDAO noteMessageDAO;
    private final FileManager fileManager;
    private final Noticer noticer;

    @Transactional
    public Integer sendMessage(EmployeeVO loginEmp, NoteMessageVO message, List<String> receivers, MultipartFile attach) throws Exception {
        if(message.getContent().isEmpty()){
            throw new RuntimeException("쪽지의 내용에는 빈 값이 올 수 없습니다.");
        }

        message.setSenderId(loginEmp.getId());
        message.setContent(message.getContent().replaceAll("<","&lt;").replaceAll("\n", "<br>"));

        Integer result = noteMessageDAO.addMessage(message);
        if(result == 0){
            return 0;
        }

        result = noteMessageDAO.addReceivers(receivers, message.getId());
        if(result == 0){
            throw new RuntimeException("발송 실패");
        }


        if(attach != null && !attach.isEmpty()) {
            FileVO file = new FileVO();
            file.setParentId(message.getId());
            file.setTblId("104");
            try {
                fileManager.uploadFile(attach, file);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException("파일 첨부 실패");
            }
        }

        noticer.sendNotice(loginEmp.getName() + "님에게 쪽지가 왔습니다", "" + message.getId(), NotificationType.NoteMessage, receivers);

        return result;
    }

    public List<NoteMessageVO> getList(EmployeeVO loginEmp, Pager pager, NoteMessageBoxType type) {
        pager.makeIndex();
        Long totalCount = noteMessageDAO.getTotalCount(loginEmp, type);

        pager.makeNum(totalCount);
        List<NoteMessageVO> list = noteMessageDAO.getList(loginEmp, pager, type);

        return list;
    }

    public List<NoteMessageVO> getSendList(EmployeeVO loginEmp, Pager pager) {
        pager.makeIndex();
        Long totalCount = noteMessageDAO.getTotalCount(loginEmp, NoteMessageBoxType.send);

        pager.makeNum(totalCount);
        List<NoteMessageVO> list = noteMessageDAO.getSendList(loginEmp, pager);

        return list;
    }

    public NoteMessageVO getMessage(NoteMessageVO noteMessage, NoteMessageBoxType type) {
        if(type.equals(NoteMessageBoxType.send)){
            NoteMessageVO message = noteMessageDAO.getMessageWithReceivers(noteMessage);
            message.getReceiversVO().forEach(e->e.setIsLeaved(false));
            return message;
        }
        return noteMessageDAO.getMessage(noteMessage);
    }

    @Transactional
    public Boolean moveBox(String id, String[] messages, NoteMessageBoxType to) {
        Integer result = noteMessageDAO.moveBox(id, messages, to);

        if(messages.length != result){
            throw new RuntimeException("box를 옮기는데 실패했습니다.");
        }

        return true;
    }

    @Transactional
    public Boolean deleteMessage(String id, String[] messages) {
        Integer result = noteMessageDAO.moveBox(id, messages, NoteMessageBoxType.completeDelete);
        if(result != messages.length){
            throw new RuntimeException("삭제 실패");
        }

        return true;
    }
}
