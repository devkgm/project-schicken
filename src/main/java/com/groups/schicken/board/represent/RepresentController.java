package com.groups.schicken.board.represent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.groups.schicken.Employee.EmployeeVO;
import com.groups.schicken.board.BoardVO;
import com.groups.schicken.common.vo.Pager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/represent/*")
@Slf4j
@RequiredArgsConstructor
public class RepresentController {


	@Autowired
	private RepresentService representService;

	@ModelAttribute("board")
	public String board() {

		return "represent";
	}



	@GetMapping("detail")
	public String getDetail(@AuthenticationPrincipal EmployeeVO employeeVO, BoardVO boardVO,Model model) throws Exception {
		boardVO.setWriterId(employeeVO.getId());
		int result = representService.hit(boardVO);

		boardVO = representService.getDetail(boardVO);
		model.addAttribute("vo", boardVO);
		
		List<BoardVO> ar = representService.pastPage(boardVO);
		model.addAttribute("move", ar);

		List<BoardVO> br = representService.nextPage(boardVO);

		model.addAttribute("next", br);


		return "board/detail";
	}

	@GetMapping("write")
	public String getWrite(Pager pager,BoardVO boardVO) throws Exception {
		
		
		return "board/write";
	}

	@PostMapping("write")
	public String getWrite(@AuthenticationPrincipal EmployeeVO employeeVO,BoardVO boardVO,@RequestParam("attach") MultipartFile attach) throws Exception {
		boardVO.setWriterId(employeeVO.getId());
		int result = representService.add(boardVO,attach);
		return "redirect:./list";
	}
	
	@GetMapping("list")
	public String getImpList(@AuthenticationPrincipal EmployeeVO employeeVO ,Pager pager,Model model,BoardVO boardVO) throws Exception {
		
		List<BoardVO> imp = representService.impList(boardVO);
		model.addAttribute("imp", imp);
		
		List<BoardVO> ar = representService.getList(pager,boardVO);
		
		model.addAttribute("list",ar);
		model.addAttribute("pager", pager);
		
		return "board/list";
	}

	@GetMapping("update")
	public String getUpdate(BoardVO boardVO,Model model)throws Exception{
		boardVO = representService.getDetail(boardVO);
		model.addAttribute("vo", boardVO);

		return "board/update";
	}

	@PostMapping("update")
	public String setUpdate(BoardVO boardVO,@RequestParam(value="attach") MultipartFile file)throws Exception{
		int result = representService.update(boardVO,file);

		return "redirect:./list";
	}

	@PostMapping("delete")
	public String delete(BoardVO boardVO)throws Exception{
		int result = representService.delete(boardVO);
		
		
		return "redirect:./list";
		
	}	

}
