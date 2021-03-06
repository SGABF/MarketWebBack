package kr.green.sga.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.green.sga.dao.AuctionDAO;
import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.BoardImageDAO;
import kr.green.sga.dao.ReplyDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.BoardImageVO;
import kr.green.sga.vo.BoardVO;
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

//	@Autowired
//	private AuctionDAO auctionDAO;

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
		String user_id = null;
//		AuctionVO auctionVO = null;
		UserVO dbUserVO = null;
		if (board_idx != 0) {
			// dbBoardVO에 게시글 하나의 객체를 담는다.
			dbBoardVO = boardDAO.selectByIdx(board_idx);
			List<BoardImageVO> boardImageList = boardImageDAO.selectByRef(dbBoardVO.getBoard_idx());
			dbBoardVO.setBoardImageList(boardImageList);
			List<ReplyVO> replyList = replyDAO.selectByRef(dbBoardVO.getBoard_idx());
			dbBoardVO.setReplyList(replyList);
			// 유저 아이디 찾기
			dbUserVO = userDAO.selectByIdx(dbBoardVO.getUser_idx());
			user_id = dbUserVO.getUser_id();
			log.info("BoardServiceImpl-selectByIdx 글 작성자 확인 : " + user_id);
			dbBoardVO.setUser_id(user_id);
//			//----------옥션----------------//
//			auctionVO = auctionDAO.selectByIdx(board_idx);
//			//----- 최고입찰자 -------------//
//			int ref = auctionDAO.selectHighUser(auctionVO.getAuction_idx());
//			userVO = userDAO.selectByIdx(ref);
//			auctionVO.setAuction_highUser(userVO.getUser_id());
//			//----- 최고입찰자 -------------//
//			dbBoardVO.setAuctionVO(auctionVO);
//			//----------옥션----------------//
		}
		log.info("BoardServiceImpl-selectByIdx 리턴 : " + dbBoardVO);
		return dbBoardVO;
	}

	@Override
	// <!-- 03. update_글 수정하기 -->
	// 토큰 보유시 동작
	public void updateBoard(BoardVO boardVO, String user_id) {
		log.info("BoardServiceImpl-updateBoard 호출 : 로그인 계정 " + user_id + " 수정 시도 게시글 " + boardVO);
		if (boardVO != null) {
			boardDAO.updateBoard(boardVO);
			log.info("BoardServiceImpl-updateBoard 게시글 수정 완료");
//			// 기존 서버 내 첨부 파일 삭제로직
//			log.info("BoardServiceImpl-updateBoard 기존 저장된 첨부파일 존재 여부를 확인합니다.");
//			List<BoardImageVO> boardImageList = boardImageDAO.selectByRef(boardVO.getBoard_idx());
//			log.info("BoardServiceImpl-updateBoard 첨부파일 확인 " + boardImageList);
//			if (boardImageList != null) {
//				log.info("BoardServiceImpl-updateBoard 기존 서버 내 첨부 파일 삭제로직 실행");
//				// db에서 첨부파일 삭제
//				for (BoardImageVO dbBoardImageVO : boardImageList) {
//					if (boardImageList != null && boardImageList.size() > 0) {
//						log.info("BoardServiceImpl-updateBoard boardImageVO.boardImageList 삭제확인" + boardImageList);
//						boardImageDAO.deleteByBoardIdx(boardVO.getBoard_idx());
//						if (os.contains("win")) {
//							path = "C:/image/";
//							log.info("wind path");
//						} else {
//							path = "/resources/Back/";
//							log.info("linux path");
//						}
//						// 서버에 저장된 첨부파일 삭제
//						File file = new File(path + File.separator + dbBoardImageVO.getBoardImage_saveName());
//						log.info("BoardServiceImpl-updateBoard 기존 서버 내 첨부 파일 삭제 진행중 file " + file);
//						file.delete();
//					}
//				}
//			}
		}
	}

	@Override
	// <!-- 04. delete_글 삭제하기 -->
	// 토큰 보유시 동작.
	// reply delete / boardImage delete / board delete
	public void deleteBoard(BoardVO boardVO, String path) {
		log.info("BoardServiceImpl-deleteBoard 호출1 : 삭제 시도 게시글 " + boardVO);
		log.info("BoardServiceImpl-deleteBoard 호출2 : 삭제 시도 경로 " + path);

		if (boardVO != null && path != null) {
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

	@Override
	public List<BoardVO> selectDescLimit() {
		log.info("BoardServiceImpl-selectDescLimit 호출 ");
		List<BoardVO> boardList = boardDAO.selectDescLimit();
		log.info("BoardServiceImpl-selectDescLimit 리턴 : " + boardList);
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

	@Override
	public List<BoardVO> searchBoardList(String keyword) {
		log.info("BoardServiceImpl-searchBoardList 호출 : 검색키워드_" + keyword);
		if (keyword != null) {
			List<BoardVO> searchList = new ArrayList<BoardVO>();
			searchList = boardDAO.searchBoardList(keyword);
			log.info("BoardServiceImpl-searchBoardList 리턴 : " + searchList);
			if (searchList.size() == 0) {
				log.info("BoardServiceImpl-searchBoardList 검색결과 없음 : null을 리턴합니다.");
				return null;
			}
			return searchList;
		} else {
			log.info("BoardServiceImpl-searchBoardList 오류 발생! 빈 List<BoardVO> 를 리턴합니다.");
			List<BoardVO> boardList = new ArrayList<BoardVO>();
			return boardList;
		}
	}

	@Override
	public List<BoardVO> selectSoldoutSellBoard() {
		log.info("BoardServiceImpl-selectSoldoutSellBoard 호출 : 판매 게시글 중 판매중인 상품만 보기");
		List<BoardVO> SoldoutSellBoard = new ArrayList<BoardVO>();
		SoldoutSellBoard = boardDAO.selectSoldoutSellBoard();
		return SoldoutSellBoard;
	}

	@Override
	public List<BoardVO> selectSoldoutAuctionBoard() {
		log.info("BoardServiceImpl-selectSoldoutAuctionBoard 호출 : 경매 게시글 중 경매 진행중인 상품만 보기");
		List<BoardVO> SoldoutAuctionBoard = new ArrayList<BoardVO>();
		SoldoutAuctionBoard = boardDAO.selectAuctionBoard();
		return SoldoutAuctionBoard;
	}

	@Override
	public void updateForSale(int board_idx, String user_id) {
		log.info("BoardServiceImpl-updateForSale 호출 board_idx : " + board_idx + ", user id : " + user_id);
		UserVO userVO = userDAO.selectUserId(user_id);
		BoardVO dbBoardVO = boardDAO.selectByIdx(board_idx);
		if (dbBoardVO.getUser_idx() == userVO.getUser_idx())
			boardDAO.updateForSale(board_idx);
	}

	@Override
	public void updateReservate(int board_idx, String user_id) {
		log.info("BoardServiceImpl-updateReservate 호출 board_idx : " + board_idx + ", user id : " + user_id);
		UserVO userVO = userDAO.selectUserId(user_id);
		BoardVO dbBoardVO = boardDAO.selectByIdx(board_idx);
		if (dbBoardVO.getUser_idx() == userVO.getUser_idx())
			boardDAO.updateReservate(board_idx);
	}

	@Override
	public void updateSoldout(int board_idx, String user_id) {
		log.info("BoardServiceImpl-updateSoldOut 호출 board_idx : " + board_idx + ", user id : " + user_id);
		UserVO userVO = userDAO.selectUserId(user_id);
		BoardVO dbBoardVO = boardDAO.selectByIdx(board_idx);
		if (dbBoardVO.getUser_idx() == userVO.getUser_idx())
			boardDAO.updateSoldout(board_idx);
	}

}
