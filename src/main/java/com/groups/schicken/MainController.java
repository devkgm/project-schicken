package com.groups.schicken;

import org.eclipse.tags.shaded.org.apache.bcel.generic.CALOAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.policy.Principal;
import com.groups.schicken.Employee.EmployeeProfileVO;
import com.groups.schicken.Employee.EmployeeService;
import com.groups.schicken.Employee.EmployeeVO;
import com.groups.schicken.annual.AnnualController;
import com.groups.schicken.board.BoardVO;
import com.groups.schicken.board.represent.RepresentService;
import com.groups.schicken.calendar.CalendarService;
import com.groups.schicken.calendar.CalendarVO;
import com.groups.schicken.common.vo.Pager;
import com.groups.schicken.document.DocumentService;
import com.groups.schicken.document.DocumentVO;
import com.groups.schicken.notification.Noticer;
import com.groups.schicken.notification.NotificationType;
import com.groups.schicken.organization.ChattingEmployeeListVO;
import com.groups.schicken.organization.OrganizationService;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;

import io.micrometer.observation.Observation.Event;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Notification;

@Controller
@RequestMapping("/")
@Slf4j
public class MainController {



	  @Autowired private CalendarService calendarService;
	  @Autowired private RepresentService representService;
	  @Autowired private EmployeeService employeeService;
	  @Autowired private OrganizationService organizationService;
	  @Autowired private Noticer noticer;
	  @Autowired private DocumentService documentService;

	/*
	 * @GetMapping("/") public String test(@RequestParam("path")String path){ return
	 * path; }
	 */


    @GetMapping("/")
    public String bordarList (Model model, String id)throws Exception{


    	EmployeeVO employeeVO = new EmployeeVO();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		id = authentication.getName();
		EmployeeProfileVO employeeProfileVO = new EmployeeProfileVO();
		employeeProfileVO.setId(id);

    	EmployeeProfileVO profile = employeeService.getProfile(id);

    	BoardVO boardVO =new BoardVO();
    	Pager pager = new Pager();
    	boardVO.setWriterId(employeeVO.getId());
		List<BoardVO> ar = representService.allgetList(pager, boardVO);
		model.addAttribute("profile", profile);
		model.addAttribute("list", ar);
		model.addAttribute("pager", pager);
		employeeVO.setId(id);
        DocumentVO documentVO = new DocumentVO();
		List<DocumentVO> dir = documentService.approvalList(employeeVO,pager,documentVO);
        model.addAttribute("dlist", dir);
        return "home";
    }


    @GetMapping("userlist")
    @ResponseBody
    public List<ChattingEmployeeListVO> main ()throws Exception{
    	// 0사번은 없으니까 다가져옴
    	List<ChattingEmployeeListVO> a = organizationService.getChattingEmployeeList("0");
		return a;
    }


    @PostMapping("insert")
    @ResponseBody
    public String insert(@RequestBody CalendarVO calendarVO) throws Exception  {
        log.info("{}", calendarVO);
        calendarVO.setUserYn(true);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        EmployeeVO employee = (EmployeeVO) authentication.getPrincipal();
        calendarVO.setShare(id);

//        if (calendarVO.getEmployeeId() == null || calendarVO.getEmployeeId().isEmpty()) {
//            // employeeId가 null일 경우 share를 이용하여 JSON 배열 형태로 생성
//            String shareValue = calendarVO.getShare();
//            List<Map<String, String>> employeeIdList = new ArrayList<>();
//            Map<String, String> employeeIdMap = new HashMap<>();
//            employeeIdMap.put("value", shareValue);
//            employeeIdList.add(employeeIdMap);
//
//            // 생성된 JSON 배열 형태를 calendarVO의 employeeId에 설정
//            Gson gson = new Gson();
//            String employeeIdJson = gson.toJson(employeeIdList);
//            calendarVO.setEmployeeId(employeeIdJson);
//
//        }

        int result = calendarService.insert(calendarVO);

        if (calendarVO.getIdList() != null && !calendarVO.getIdList().isEmpty()) {
            noticer.sendNotice(employee.getName() + "님의 일정이 공유되었습니다.", "/", NotificationType.Calendar, calendarVO.getIdList());
        }

        return "일정이 성공적으로 추가되었습니다.";
    }
    @PostMapping("update")
    @ResponseBody
    public String update(@RequestBody CalendarVO calendarVO,Long id)throws Exception{
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String ida = authentication.getName();
		calendarVO.setEmployeeId(ida);
		calendarVO.setShare(ida);
		calendarVO.setCalendarId(calendarVO.getId());
    	calendarService.update(calendarVO);
    	return "업데이트 성공";
    }




    @GetMapping("list")
    @ResponseBody
    public List<CalendarVO> list (CalendarVO calendarVO)throws Exception{
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
    	calendarVO.setEmployeeId(id);
    	calendarVO.setShare(id);
    	List<CalendarVO> ar = calendarService.getList(calendarVO);
    	return ar;
    }
    @GetMapping("share")
    @ResponseBody
    public List<CalendarVO> share (CalendarVO calendarVO)throws Exception{
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
    	calendarVO.setEmployeeId(id);
    	calendarVO.setShare(id);
    	List<CalendarVO> ar = calendarService.share(calendarVO);
    	return ar;
    }



    @GetMapping("detail")
    @ResponseBody
    public CalendarVO detail(CalendarVO calendarVO, Model model, HttpSession session,Long id)throws Exception{

    	calendarVO.setId(id);
    	 calendarService.detail(calendarVO);

    	model.addAttribute("detailMain", calendarVO);

    	return calendarService.detail(calendarVO);
    }


    @PostMapping("calendarDelete")
    @ResponseBody
    public String calendarDelete (@RequestBody CalendarVO calendarVO) throws Exception {
        Long calendar_id = calendarVO.getCalendarId(); // CalendarVO에서 calendar_id 값을 가져옴
        calendarVO.setCalendarId(calendar_id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        calendarVO.setEmployeeId(id);
        calendarService.calendarDelete(calendarVO);
        return "성공";
    }




    @PostMapping("calUpdate")
    @ResponseBody
    public String calUpdate(@RequestBody CalendarVO calendarVO)throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        calendarVO.setEmployeeId(id);
    	calendarService.calUpdate(calendarVO);

		calendarVO.setShare(id);
        calendarVO.setUserYn(true);
        int result = calendarService.insert2(calendarVO);
    	return "성공";
    }




}
