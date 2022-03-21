package kr.green.sga.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.BoardImageDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.BoardImageVO;
import lombok.extern.slf4j.Slf4j;

@Service("boardImageService")
@Slf4j
public class BoardImageServiceImpl implements BoardImageService {

	@Autowired
	private BoardImageDAO boardImageDAO;

	@Autowired
	private BoardDAO boardDAO;

	@Autowired
	private UserDAO userDAO;

	@Override
	public void insertBoardImage(BoardImageVO boardImageVO) {
		log.info("BoardImageServiceImpl-insertBoardImage 호출 : " + boardImageVO);
		if (boardImageVO != null) {
				boardImageDAO.insertBoardImage(boardImageVO);
				log.info("BoardImageServiceImpl-insertBoardImage 리턴 : 게시글 첨부 이미지 저장완료 " + boardImageVO);
			} 
		}

	@Override
	public BoardImageVO selectByIdx(int boardImage_idx) {
		log.info("BoardImageServiceImpl-selectByIdx 호출 : " + boardImage_idx);
		BoardImageVO dbBoardImageVO = null;
		if (boardImage_idx != 0) {
			dbBoardImageVO = boardImageDAO.selectByIdx(boardImage_idx);
			log.info("BoardImageServiceImpl-deleteBoardImage 첨부파일 확인 " + dbBoardImageVO);
		}
		log.info("BoardImageServiceImpl-selectByIdx 리턴 : " + dbBoardImageVO);
		return dbBoardImageVO;
	}

	@Override
	public void deleteBoardImage(int boardImage_idx) {
		log.info("BoardImageServiceImpl-deleteBoardImage 호출 : " + boardImage_idx + "번째 첨부파일");
		BoardImageVO dbBoardImageVO = null;
		dbBoardImageVO = boardImageDAO.selectByIdx(boardImage_idx); //
		if (dbBoardImageVO.getBoard_idx() != 0 && dbBoardImageVO.getBoardImage_idx() != 0) {
			log.info("BoardServiceImpl-deleteBoard 호출 : " + dbBoardImageVO.getBoard_idx() + "번째 게시글의 " + boardImage_idx
					+ "번째 첨부파일 확인");
			boardImageDAO.deleteBoardImage(boardImage_idx);
		} else {
			log.info("BoardImageServiceImpl-deleteBoardImage 잘못된 접근");
		}
		log.info("BoardImageServiceImpl-deleteBoardImage 이미지 삭제 완료");
	}

	@Override
	public List<BoardImageVO> selectByRef(int board_idx) {
		log.info("BoardImageServiceImpl-selectByRef 호출 : " + board_idx);
		List<BoardImageVO> list = null;
		if (board_idx != 0) {
			list = boardImageDAO.selectByRef(board_idx);
		}
		log.info("BoardImageServiceImpl-selectByRef 리턴 : " + list);
		return list;
	}

	@Override
	public List<BoardImageVO> selectList() {
		log.info("BoardImageServiceImpl-selectList 호출");
		List<BoardImageVO> list = null;
		list = boardImageDAO.selectList();
		if (list == null) {
			log.info("BoardImageServiceImpl-selectList 빈 VO객체 리턴함.");
			list = new ArrayList<BoardImageVO>();
		}
		log.info("BoardImageServiceImpl-selectList 리턴 : " + list);
		return list;
	}

}
