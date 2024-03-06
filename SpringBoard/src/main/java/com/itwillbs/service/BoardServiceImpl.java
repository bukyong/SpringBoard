package com.itwillbs.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.BoardVO;
import com.itwillbs.domain.Criteria;
import com.itwillbs.persistence.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {

	// DAO 객체 주입
	@Inject
	private BoardDAO bdao;
	
	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	@Override
	public void regist(BoardVO vo) throws Exception {
		logger.debug(" regist(BoardVO vo) 실행 -> DAO 글쓰기 동작 호출 ");
		
		bdao.boardCreate(vo);
		
		logger.debug(" 서비스 동작 완료 -> 컨트롤러 이동 ");
	}

	@Override
	public List<BoardVO> getList() throws Exception {
		logger.debug(" getList() 실행 -> DAO 글 목록 조회 동작 호출 ");
		
		return bdao.boardListSelect();
	}

	@Override
	public BoardVO read(Integer bno) throws Exception {
		logger.debug(" getBoard(BoardVO vo) 실행 -> DAO 글쓰기 동작 호출 ");
		
		BoardVO resultVO = bdao.boardSelect(bno);
		
		return resultVO;
	}

	@Override
	public void updateViewcnt(Integer bno) throws Exception {
		logger.debug(" updateViewcnt(Integer bno) 실행 -> DAO 글쓰기 동작 호출 ");
		
		bdao.boardViewcntUpdate(bno);
	}

	@Override
	public void modifyBoard(BoardVO vo) throws Exception {
		logger.debug(" update(BoardVO vo) 실행 -> DAO 글 수정 동작 호출 ");
		
		bdao.boardUpdate(vo);
	}

	@Override
	public void deleteBoard(Integer bno) throws Exception {
		logger.debug(" deleteBoard(BoardVO vo) 실행 -> DAO 글 삭제 동작 호출 ");
		
		bdao.boardDelete(bno);
	}

	@Override
	public List<BoardVO> getListCri(Criteria cri) throws Exception {
		logger.debug(" getListCri(Criteria cri) 실행 -> DAO 글 목록 조회 동작(cri) 호출 ");
		
		return bdao.boardListCriSelect(cri);
	}

	@Override
	public int getBoardListCount() throws Exception {
		logger.debug("  getBoardListCount() 호출 ");
		
		return bdao.boardListCount();
	}
	
}