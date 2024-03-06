package com.itwillbs.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwillbs.domain.BoardVO;
import com.itwillbs.domain.Criteria;
import com.itwillbs.domain.PageVO;
import com.itwillbs.service.BoardService;

@Controller
@RequestMapping(value = "/board/*")
public class BoardController {
	
	// Service 객체 주입
	@Inject
	private BoardService bService;

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	// 글쓰기 GET : /board/register
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void registerGET() throws Exception {
		logger.debug("/board/register -> registerGET() 호출");
		logger.debug("/board/register.jsp 뷰 연결");
		
	}
	
	// 글쓰기 POST : /board/register
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerPOST(BoardVO vo) throws Exception {
		logger.debug("/board/register -> registerPOST() 호출");
		
		// 한글처리(필터) 생략
		
		// 전달정보(글 정보) 저장
		logger.debug(" 전달정보 : "+vo);
		
		// 서비스 -> DAO 글쓰기 동작 호출
		bService.regist(vo);
		logger.debug(" 글쓰기 완료! -> 리스트 페이지로 이동 ");
		
		// 페이지 이동(list)
		return "redirect:/board/list";
		
	}
	
	// 리스트 GET : /board/list
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void listGET(Model model, HttpSession session) throws Exception {
		logger.debug("/board/list -> listGET() 호출");
		logger.debug("/board/list.jsp 뷰 연결");
		
		// 서비스 -> DAO 게시판 글 목록 가져오기
		List<BoardVO> boardList = bService.getList();
		logger.debug(" list.size : "+boardList.size());
		
		// 연결된 뷰페이지로 전달(Model)
		model.addAttribute("boardList", boardList);
		
		// 조회수 상태 0 : 조회수 증가 X, 1 : 조회수 증가 O
		session.setAttribute("viewUpdateStatus", 1);
		
		//return "/board/list";
	}
	
	// 본문읽기 GET : /board/read?bno=000
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public void readGET(Criteria cri, @RequestParam("bno") int bno, Model model, 
			HttpSession session) throws Exception {
		// @ModelAttribute : 파라메터 저장 + 영역 저장 (1:n 관계)
		// @RequestParam : 파라메터 저장 (1:1 관계)
		
		logger.debug("/board/read -> readGET() 호출");
		logger.debug("/board/read.jsp 뷰 연결");
		
		// 전달정보 저장
		logger.debug(" bno : "+bno);
		
		int status = (Integer)session.getAttribute("viewUpdateStatus");
		
		if(status == 1) {
			// 서비스 -> DAO 게시판 글 조회수 1 증가
			bService.updateViewcnt(bno);
			
			// 조회수 상태 0 : 조회수 증가 X, 1 : 조회수 증가 O
			session.setAttribute("viewUpdateStatus", 0);
		}
		
		// 서비스 -> DAO 게시판 글 정보 조회 동작
		BoardVO vo = bService.read(bno);

		// 연결된 뷰페이지로 전달(Model)
		model.addAttribute("vo", vo);
		// model.addAttribute(bService.read(bno));
		
		model.addAttribute("cri", cri); //뷰페이지로 페이징처리 정보를 전달
		
		// 뷰페이지로 이동 (/board/read)	
	}
	
	// 본문수정 GET : /board/modify?bno=000
	// 본문수정 GET : /board/modify?bno=000&page=000&pageSize=000
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void modifyGET(Criteria cri, @RequestParam("bno") int bno, Model model, 
			HttpSession session) throws Exception {
		logger.debug("/board/modify -> modifyGET() 호출");
		logger.debug("/board/modify.jsp 뷰 연결");
		
		// 전달정보(bno) 저장
		logger.debug(" bno : "+bno);
		
		// 서비스 -> DAO 특정 글 정보 조회 동작
		model.addAttribute(bService.read(bno));
		model.addAttribute("cri", cri);
		
		// 연결된 뷰페이지로 전달(Model)

		// 뷰페이지로 이동 (/board/modify)	
	}
	
	// 본문수정 POST : /board/modify
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifyPOST(Criteria cri, BoardVO vo) throws Exception {
		logger.debug("/board/modify -> modifyPOST() 호출");
		
		// 한글처리 인코딩
		// 전달정보 저장(bno, title, writer, content)
		logger.debug(" BoardVO : "+vo);
		
		// 서비스 -> DAO 게시판 글 수정 동작
		bService.modifyBoard(vo);
		
		// 수정 완료 후 list 페이지로 이동 (redirect)
		//return "redirect:/board/list";
		return "redirect:/board/listCri?page="+cri.getPage()+"&pageSize="+cri.getPageSize();
	}
	
	// 본문삭제 POST : /board/remove
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public String deletePOST(RedirectAttributes rttr, Criteria cri, @RequestParam("bno") int bno) throws Exception {
		logger.debug("/board/remove -> deletePOST() 호출");
		
		// 전달정보(bno) 저장
		logger.debug(" bno : "+bno);
		
		// 서비스 -> DAO 특정 글 삭제 조회 동작
		bService.deleteBoard(bno);

		try {
			// 예외가 발생할지도 모르는 코드
		} catch (Exception e) {
			// 예외 처리-출력
		}

		//rttr.addFlashAttribute("cri", cri);
		//rttr.addFlashAttribute("page", cri.getPage());
		//rttr.addFlashAttribute("pageSize", cri.getPageSize());
		
		// 페이지 이동 ( /board/list )
		//return "redirect:/board/list";
		return "redirect:/board/listCri?page="+cri.getPage()+"&pageSize="+cri.getPageSize();
		//return "redirect:/board/listCri";
	}
	
	// http://localhost:8088/board/listCri
	// http://localhost:8088/board/listCri?page=2
	// http://localhost:8088/board/listCri?pageSize=30
	// http://localhost:8088/board/listCri?page=2&pageSize=40
	// 리스트GET : /board/listCri
	@RequestMapping(value = "/listCri", method = RequestMethod.GET)
	public void listCriGET(Criteria cri, Model model, HttpSession session) throws Exception {
		logger.debug(" /board/listCri -> listCriGET()실행 ");
		logger.debug(" /board/listCri.jsp 연결 ");
		
		// 페이징처리 객체
//		Criteria cri = new Criteria();
//		cri.setPageSize(20);
		PageVO pageVO = new PageVO();
		pageVO.setCri(cri);
		//pageVO.setTotalCount(510); // 총 개수 직접 계산
		pageVO.setTotalCount(bService.getBoardListCount());	// SQL 구문 계산
		
		// 서비스 -> DAO 게시판 글 목록 가져오기
		//List<BoardVO> boardList = bService.getList(); // all
		List<BoardVO> boardList = bService.getListCri(cri); // 페이징
		
		logger.debug(" list.size : " + boardList.size());
		// 연결된 뷰페이지에 정보 전달(Model)
		model.addAttribute("boardList", boardList);
		
		model.addAttribute("cri", cri); // 페이징 처리 정보 전달
		
		model.addAttribute("pageVO", pageVO);
		
		// 조회수 상태 0 : 조회수 증가 X, 1 : 조회수 증가 O
		session.setAttribute("viewUpdateStatus", 1);
	}
}
