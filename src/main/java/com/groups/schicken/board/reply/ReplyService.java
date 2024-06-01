package com.groups.schicken.board.reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groups.schicken.common.util.DateManager;
import com.groups.schicken.common.vo.Pager;

@Service
public class ReplyService {
	@Autowired
	private ReplyDAO replyDAO;
	
	
	public List<ReplyVO> list(ReplyVO replyVO,Pager pager)throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		
		map.put("pager", pager);
		map.put("replyVO",replyVO);
		
		pager.makeIndex();
		pager.makeNum(replyDAO.totalCount(map));
		
		
		return replyDAO.list(map);
		
	}
	
	public int add(ReplyVO replyVO)throws Exception{
		
		replyVO.setDate(DateManager.getTodayDate());
		
		int result = replyDAO.add(replyVO);
		
		return result;
	}
	
	public int update(ReplyVO replyVO)throws Exception{
		int result = replyDAO.update(replyVO);
		
		return result;
	}
	
	public int delete(ReplyVO replyVO)throws Exception{
		int result = replyDAO.delete(replyVO);
		
		return result;
	}
}
