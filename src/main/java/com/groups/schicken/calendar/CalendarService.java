package com.groups.schicken.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groups.schicken.Employee.EmployeeVO;
import com.groups.schicken.annual.AnnualService;
import com.groups.schicken.notification.Noticer;
import com.groups.schicken.notification.NotificationType;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;

import io.micrometer.observation.Observation.Event;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CalendarService {


	  private static final int result = 0;
	@Autowired
	  private CalendarDAO calendarDAO;
	  @Autowired private Noticer noticer;
    @Qualifier("eagerStompWebSocketHandlerMapping")
    @Autowired
    private LazyInitializationExcludeFilter eagerStompWebSocketHandlerMapping;

	public int insert2(CalendarVO calendarVO) throws Exception {
		calendarDAO.insert(calendarVO);
		  calendarVO.setCalendarId(calendarVO.getId());
		int result = calendarDAO.insertuser(calendarVO);
		return result;
	}


	public int insert(CalendarVO calendarVO) throws Exception {

	    // CalendarDAO를 사용하여 캘린더 이벤트를 삽입
	    calendarDAO.insert(calendarVO);

	    // 삽입된 캘린더 이벤트의 ID를 CalendarVO에 설정
	    calendarVO.setCalendarId(calendarVO.getId());
	    if(calendarVO.getEmployeeId()==null) {
	    	calendarVO.setEmployeeId(calendarVO.getShare());
	    	calendarDAO.insertuser(calendarVO);
	    }
	    // Gson을 사용하여 JSON 문자열을 배열로 변환
	    Gson gson = new Gson();
	    String employeeIdJson = calendarVO.getEmployeeId();
	    List<Map<String, String>> employeeIdList = gson.fromJson(employeeIdJson, new TypeToken<List<Map<String, String>>>() {}.getType());

	    // 배열의 각 요소를 추출하여 인서트
	    boolean shareExists = false;
	    List<String> idList = new ArrayList<>(); // idList 초기화

			//알림
		if(employeeIdList != null){
	        for (Map<String, String> employeeIdMap : employeeIdList) {
	            String employeeIdValue = employeeIdMap.get("value");
	            if (!employeeIdValue.equals(calendarVO.getShare())) { // share와 같은 값은 제외
	                idList.add(employeeIdValue); // idList에 추가
	            }
	            if (employeeIdValue.equals(calendarVO.getShare())) {
	                shareExists = true;
	            }
	        }


	    for (Map<String, String> employeeIdMap : employeeIdList) {
	        String employeeIdValue = employeeIdMap.get("value");
			calendarVO.setEmployeeId(calendarVO.getShare());
	        calendarVO.setEmployeeId(employeeIdValue);
	        int result = calendarDAO.insertuser(calendarVO);
	    }
		}
		if(calendarVO.getEmployeeId()!=calendarVO.getShare()){
			calendarVO.getCalendarId();
			calendarVO.setEmployeeId(calendarVO.getShare());
			calendarVO.setUserYn(true);
			calendarDAO.insertuser(calendarVO);
		}
	    // idList를 CalendarVO의 idList에 설정
	    calendarVO.setIdList(idList);



		  // 부서 리스트를 조회하여 CalendarVO에 설정하고 인서트
	    List<CalendarVO> a = calendarDAO.departmentList(calendarVO);
	    if (!a.isEmpty()) {
	        List<String> departmentEmployeeIds = new ArrayList<>(); // 부서의 employeeId를 저장할 리스트 생성
	        for (CalendarVO departmentCalendarVO : a) {
	            String departmentEmployeeId = departmentCalendarVO.getEmployeeId();
	            departmentEmployeeIds.add(departmentEmployeeId); // 부서의 employeeId를 리스트에 추가
	        }
	        calendarVO.setCalendarId(calendarVO.getId());
	        calendarDAO.insertAllUser(a); // 부서의 캘린더 이벤트를 추가
	        List<CalendarVO> az = calendarDAO.depList(calendarVO);
	        for (CalendarVO item : az) {
	            calendarDAO.depDelte(item.getEmployeeId());
	        }
	        // 부서의 employeeId 리스트를 CalendarVO의 idList에 추가
	        if (calendarVO.getIdList() == null) {
	            calendarVO.setIdList(departmentEmployeeIds);
	        } else {
	            calendarVO.getIdList().addAll(departmentEmployeeIds);
	        }
	    }

	   // calendarDAO.depDelte(calendarVO);



	    return result;
	}


	public int update (CalendarVO calendarVO)throws Exception{

		CalendarVO a = calendarDAO.info(calendarVO);
		calendarVO.setContent(a.getContent());
		calendarVO.setTitle(a.getTitle());
		calendarVO.setEmployeeId(a.getEmployeeId());
		calendarDAO.insert2(calendarVO);
		calendarDAO.calUpdate(calendarVO);
		calendarVO.setCalendarId(calendarVO.getId());
		calendarVO.setUserYn(true);
		calendarDAO.insertuser(calendarVO);
		return 0;
	}



	 public List<CalendarVO> getList(CalendarVO calendarVO)throws Exception{
		 return calendarDAO.calList(calendarVO);
	 }
	 public List<CalendarVO> share(CalendarVO calendarVO)throws Exception{
		 return calendarDAO.share(calendarVO);
	 }


	 public CalendarVO detail (CalendarVO calendarVO)throws Exception{
		 return calendarDAO.detail(calendarVO);
	 }

	public int calendarDelete (CalendarVO calendarVO)throws Exception{
		return calendarDAO.calendarDelete(calendarVO);
	}

	public int calUpdate (CalendarVO calendarVO)throws Exception{
		return calendarDAO.calUpdate(calendarVO);
	}


}
