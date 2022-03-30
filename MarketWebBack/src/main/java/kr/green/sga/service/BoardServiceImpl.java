package kr.green.sga.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.BoardImageDAO;
import kr.green.sga.dao.ReplyDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.BoardImageVO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.ReplyImageVO;
import kr.green.sga.vo.ReplyVO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service("boardService")
public class BoardServiceImpl implements BoardService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private BoardDAO boardDAO;

	@Autowired
	private BoardImageDAO boardImageDAO;

	@Autowired
	private ReplyDAO replyDAO;

	private String os = System.getProperty("os.name").toLowerCase();

	@Override
	// <!-- 01. insert_글 쓰기 -->
	// 토큰 보유시 동작
	public void insertBoard(BoardVO boardVO, String user_id) {
		log.info("BoardServiceImpl-insertBoard 호출 : 현 로그인 계정 " + user_id + " 게시글 작성시도 : " + boardVO + "\n");
		UserVO dbUserVO = userDAO.selectUserId(user_id); // 유저 db VO
		// boardVO에 넘겨 받은 값이 있다면
		if (boardVO != null && dbUserVO.getUser_id().equals(user_id)) {
			boardVO.setUser_idx(dbUserVO.getUser_idx()); // boardVO의 user_idx 외래키 값을 수동 set처리.
			boardDAO.insertBoard(boardVO);
			log.info("BoardServiceImpl-insertBoard 리턴 : 게시글 작성 완료 boardVO " + boardVO + "\n");
		}
	}

	@Override
	// <!-- 02. select_글 1개 가져오기 -->
	// 토큰 보유 / 미보유 둘다 동작
	public BoardVO selectByIdx(int board_idx) {
		log.info("BoardServiceImpl-selectByIdx 호출 : " + board_idx);
		BoardVO dbBoardVO = null;
		BoardImageVO dbBoardImageVO = null;
		if (board_idx != 0) {
			// dbBoardVO에 게시글 하나의 객체를 담는다.
			dbBoardVO = boardDAO.selectByIdx(board_idx);
			List<BoardImageVO> boardImageList = boardImageDAO.selectByRef(dbBoardVO.getBoard_idx());
			log.info("boardImageList 테스트 : " + boardImageList);
			boardImageList.add(dbBoardImageVO);
//			boardImageList.get(0).get
			dbBoardVO.setBoardImageList(boardImageList);
		}
		log.info("BoardServiceImpl-selectByIdx 리턴 : " + dbBoardVO);
		return dbBoardVO;
	}

	@Override
	// <!-- 03. update_글 수정하기 -->
	// 토큰 보유시 동작
	public void updateBoard(BoardVO boardVO, String user_id) {
		log.info("BoardServiceImpl-updateBoard 호출 : 로그인 계정 " + user_id + " 수정 시도 게시글 " + boardVO);
		BoardVO dbBoardVO = null;
		UserVO dbUserVO = null;
		String path = "";
		if (boardVO != null) {
			dbBoardVO = boardDAO.selectByIdx(boardVO.getBoard_idx()); // 보드 디비 원본
			log.info("BoardServiceImpl-updateBoard 수정 전 원본 글 확인 : " + dbBoardVO);
			boardDAO.updateBoard(boardVO);
			log.info("BoardServiceImpl-updateBoard 게시글 수정 완료");
			// 기존 서버 내 첨부 파일 삭제로직
			log.info("BoardServiceImpl-updateBoard 기존 저장된 첨부파일 존재 여부를 확인합니다.");
			List<BoardImageVO> boardImageList = boardImageDAO.selectByRef(boardVO.getBoard_idx());
			log.info("BoardServiceImpl-updateBoard 첨부파일 확인 " + boardImageList);
			if (boardImageList != null) {
				log.info("BoardServiceImpl-updateBoard 기존 서버 내 첨부 파일 삭제로직 실행");
				// db에서 첨부파일 삭제
				for (BoardImageVO dbBoardImageVO : boardImageList) {
					if (boardImageList != null && boardImageList.size() > 0) {
						log.info("BoardServiceImpl-updateBoard boardImageVO.boardImageList 삭제확인" + boardImageList);
						boardImageDAO.deleteByBoardIdx(boardVO.getBoard_idx());
						if (os.contains("win")) {
							path = "C:/image/";
							log.info("wind path");
						} else {
							path = "/resources/Back/";
							log.info("linux path");
						}
						// 서버에 저장된 첨부파일 삭제
						File file = new File(path + File.separator + dbBoardImageVO.getBoardImage_saveName());
						log.info("BoardServiceImpl-updateBoard 기존 서버 내 첨부 파일 삭제 진행중 file " + file);
						file.delete();
					}
				}
			}
		}
		log.info("BoardServiceImpl-updateBoard 리턴 : 최종 수정 완료 및 첨부파일 삭제 완료.");
	}

	@Override
	// <!-- 04. delete_글 삭제하기 -->
	// 토큰 보유시 동작.
	// reply delete / boardImage delete / board delete
	public void deleteBoard(BoardVO boardVO, String path) {
		log.info("BoardServiceImpl-deleteBoard 호출1 : 삭제 시도 게시글 " + boardVO);
		log.info("BoardServiceImpl-deleteBoard 호출2 : 삭제 시도 경로 " + path);
		if (boardVO != null && path != null) {
			UserVO boardUserVO = userDAO.selectByIdx(boardVO.getUser_idx());
			List<BoardImageVO> boardImageList = boardImageDAO.selectByRef(boardVO.getBoard_idx());
			if (boardImageList != null && boardImageList.size() > 0) {
				log.info("BoardServiceImpl-deleteBoard 첨부 이미지 리스트 확인 및 삭제 " + boardImageList);
				for (BoardImageVO boardImageVO : boardImageList) {
					// DB 파일 삭제
					boardImageDAO.deleteByIdx(boardImageVO.getBoardImage_idx());
					// 실제 파일삭제
					File file = new File(path + File.separator + boardImageVO.getBoardImage_saveName());
					file.delete();
				}
			}
			List<ReplyVO> replyList = replyDAO.selectByRef(boardVO.getBoard_idx());
			if (replyList != null) {
				log.info("BoardServiceImpl-deleteBoard 댓글 리스트 확인 및 삭제 " + replyList);
				// 댓글 삭제
				replyDAO.deleteByBoardIdx(boardVO.getBoard_idx());
			}
		}
		boardDAO.deleteBoard(boardVO.getBoard_idx());
		log.info("BoardServiceImpl-deleteBoard 게시글 삭제 완료 ");
	}

	@Override
	// <!-- 50. select_전체 개수얻기 -->
	// 토큰 보유 / 미보유 둘다 동작
	public int selectCount() {
		log.info("BoardServiceImpl-selectCount 호출 ");
		int count = 0;
		count = boardDAO.selectCount();
		if (count == 0) {
			log.info("BoardServiceImpl-selectCount 카운트 없음.");
			;
		}
		log.info("BoardServiceImpl-selectCount 리턴 " + count);
		return count;
	}

	@Override
	// <!-- 51. select_한페이지 글 목록 가져오기 -->
	// 토큰 보유 / 미보유 둘다 동작
	public List<BoardVO> selectList() {
		log.info("BoardServiceImpl-selectList 호출");
		List<BoardVO> list = null;
		list = boardDAO.selectList();
		if (list == null) {
			log.info("BoardServiceImpl-selectList 등록된 글이 없음. 빈 VO객체 리턴함.");
			list = new ArrayList<BoardVO>();
		}
		log.info("BoardServiceImpl-selectList 리턴 : " + list);
		return list;
	}

	@Override
	// <!-- 52. 마지막에 저장한 글의 idx를 읽어오는 쿼리 -->
	// 토큰 보유 / 미보유 둘다 동작
	public int selectMaxIdx() {
		log.info("BoardServiceImpl-selectMaxIdx 호출 ");
		int maxIdx = 0;
		maxIdx = boardDAO.selectMaxIdx();
		if (maxIdx == 0) {
			log.info("BoardServiceImpl-selectMaxIdx idx 값 0.");
			;
		}
		log.info("BoardServiceImpl-selectMaxIdx 리턴 " + maxIdx);
		return maxIdx;
	}
//
//	@Override
//	public List<BoardVO> selectDescLimit() {
//		List<BoardVO> boardList = new ArrayList<BoardVO>();
//		int boardMaxIdx = boardDAO.selectMaxIdx();
//		BoardVO dbBoardVO = boardDAO.selectByIdx(boardMaxIdx);
//		
//		
//		
//		return boardList;
//	}

//	원본
	@Override
	public List<BoardVO> selectDescLimit() {
		List<BoardVO> boardList = new ArrayList<BoardVO>();
		log.info("BoardServiceImpl-selectDescLimit 호출 : List<BoardVO> 생성 " + boardList + "\n");
		int boardMaxIdx = boardDAO.selectMaxIdx();
		log.info("BoardServiceImpl-selectDescLimit boardMaxIdx 값 확인 / " + boardMaxIdx);
		for (int i = 8; i > 0; i--) {
			BoardVO dbBoardVO = boardDAO.selectByIdx(boardMaxIdx);
			if (dbBoardVO == null) {
				boardMaxIdx--;
				log.info("BoardServiceImpl-selectDescLimit dbBoardVO == null 로 인한 idx-- 처리 / " + boardMaxIdx);
				dbBoardVO = boardDAO.selectByIdx(boardMaxIdx);
			}
			if (dbBoardVO == null) {
				break;
			}
			log.info("BoardServiceImpl-selectDescLimit boardMaxIdx로 생성한 dbBoardVO / " + dbBoardVO);
			List<BoardImageVO> dbBoardImageList = boardImageDAO.selectByRef(boardMaxIdx);
			log.info("BoardServiceImpl-selectDescLimit boardMaxIdx로 생성한 dbBoardVO의 dbBoardImageList 컬럼 / "
					+ dbBoardImageList);
			dbBoardVO.setBoardImageList(dbBoardImageList);
			log.info("BoardServiceImpl-selectDescLimit dbBoardImageList 컬럼을 넣은 dbBoardVO / " + dbBoardVO);
			boardList.add(dbBoardVO);
			log.info("BoardServiceImpl-selectDescLimit 해당 dbBoardVO를 BoardList에 추가 / " + boardList);
			boardMaxIdx--;
			log.info("BoardServiceImpl-selectDescLimit boardMaxIdx 값 변경 / " + boardMaxIdx);
		}
		log.info("BoardServiceImpl-selectDescLimit 최종 리턴 : " + boardList + "\n");
		return boardList;
	}

	@Override
	public List<BoardVO> selectSellBoard() {
		log.info("BoardServiceImpl-selectSellBoard 호출 ");
		List<BoardVO> sellBoardList = new ArrayList<BoardVO>();
		sellBoardList = boardDAO.selectSellBoard();
		return sellBoardList;
	}

	@Override
	public List<BoardVO> selectBuyBoard() {
		List<BoardVO> buyBoardList = new ArrayList<BoardVO>();
		buyBoardList = boardDAO.selectBuyBoard();
		return buyBoardList;
	}

	@Override
	public List<BoardVO> selectAuctionBoard() {
		List<BoardVO> auctionBoardList = new ArrayList<BoardVO>();
		auctionBoardList = boardDAO.selectAuctionBoard();
		return auctionBoardList;
	}

}
