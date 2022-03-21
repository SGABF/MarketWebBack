package kr.green.sga.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.BoardImageDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.BoardImageVO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service("boardService")
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDAO boardDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private BoardImageDAO boardImageDAO;

//	@Autowired
//	private 

	@Override
	// <!-- 01. insert_글 쓰기 -->
	// 토큰 보유시 동작
	public void insertBoard(BoardVO boardVO, String user_id) {
		log.info("BoardServiceImpl-insertBoard 호출 : 현 로그인 계정 " + user_id + ", 게시글 작성시도 : " + boardVO + "\n");
		UserVO originUserVO = null;
		BoardVO saveBoardVO = null;
		// boardVO에 넘겨 받은 값이 있다면
		if (boardVO != null) {
			originUserVO = userDAO.selectUserId(user_id);
			boardVO.setUser_idx(originUserVO.getUser_idx());
			log.info("BoardServiceImpl-insertBoard boardVO의 user_idx 외래키 조회 boardVO.getUser_idx : "
					+ boardVO.getUser_idx() + ", originBoardVO.getUser_idx : " + originUserVO.getUser_idx());
			if (originUserVO != null && originUserVO.getUser_id().equals(user_id)) {
				boardDAO.insertBoard(boardVO);
				log.info("BoardServiceImpl-insertBoard 게시글 저장 완료 : " + boardVO);
			} else {
				log.info("BoardServiceImpl-insertBoard originUserVO.getUser_idx() != boardVO.getUser_idx()");
			}
			if (boardVO.getBoardImageList() != null) {
				int ref = boardDAO.selectMaxIdx();
				log.info("BoardServiceImpl-insertBoard 게시글의 첨부 이미지 확인 : " + boardVO.getBoardImageList());
				for (BoardImageVO boardImageFile : boardVO.getBoardImageList()) {
//					boardImageFile.setBoard_idx(ref);
					boardImageDAO.insertBoardImage(boardImageFile);
					log.info("BoardServiceImpl-insertBoard 게시글의 첨부 이미지 저장 완료 : " + ref + " idx 글에 "
							+ boardVO.getBoardImageList() + "첨부이미지 저장완료");
				}
			} else {
				saveBoardVO = boardDAO.selectByIdx(selectMaxIdx());
				log.info("BoardServiceImpl-insertBoard 게시글의 첨부 이미지 없음. 첨부 이미지가 없는 게시글 저장완료 저장된 게시글 : " + saveBoardVO);
			}
		}
	}

	@Override
	// <!-- 02. select_글 1개 가져오기 -->
	// 토큰 보유 / 미보유 둘다 동작
	public BoardVO selectByIdx(int board_idx) {
		log.info("BoardServiceImpl-selectByIdx 호출 : " + board_idx);
		BoardVO boardVO = null;
		if (board_idx != 0) {
			log.info("BoardServiceImpl-selectByIdx 게시글 확인 : " + board_idx + "번째 게시글");
			boardVO = boardDAO.selectByIdx(board_idx);
			if (boardVO != null) {
				boardVO.setBoardImageList(boardImageDAO.selectByRef(boardVO.getBoard_idx()));
			}
		}
		log.info("BoardServiceImpl-selectByIdx 리턴 : " + boardVO);
		return boardVO;
	}

	@Override
	// <!-- 03. update_글 수정하기 -->
	// 토큰 보유시 동작
	public void updateBoard(BoardVO boardVO, String path, String[] delfile, String user_id) {
		log.info("BoardServiceImpl-updateBoard 호출 : " + boardVO + "\n 로그인 계정 : " + user_id);
		BoardVO originBoardVO = null;
		UserVO userVO = null;
		if (boardVO != null) {
			originBoardVO = boardDAO.selectByIdx(boardVO.getBoard_idx()); // 보드 디비 원본
			userVO = userDAO.selectByIdx(boardVO.getUser_idx()); // 현재 접속한 유저의 디비
			if (originBoardVO != null && userVO.getUser_id().equals(user_id)) {
				log.info("BoardServiceImpl-updateBoard 수정 전 원본 글 확인");
				log.info("BoardServiceImpl-updateBoard 원본 글의 작성자와 로그인 계정의 일치여부 확인");
				boardDAO.updateBoard(boardVO);
				log.info("BoardServiceImpl-updateBoard 게시글 수정 완료 : " + boardVO + "\n첨부파일 존재여부 확인시도");
				if (boardVO.getBoardImageList() != null) {
					log.info("BoardServiceImpl-updateBoard 첨부파일 존재확인");
					for (BoardImageVO boardImageVO : boardVO.getBoardImageList()) {
						boardImageVO.setBoard_idx(boardVO.getBoard_idx());
						log.info("BoardServiceImpl-updateBoard 게시글에 이미지 첨부");
						boardImageDAO.insertBoardImage(boardImageVO);
					}
				}
				// 기존 서버 내 첨부 파일 삭제로직
				log.info("BoardServiceImpl-updateBoard 기존 서버 내 첨부 파일 삭제로직 진행");
				if (delfile != null) {
					for (String idx : delfile) {
						// db에서 해당 번호 파일의 정보를 읽어온다
						BoardImageVO imageVO = boardImageDAO.selectByIdx(Integer.parseInt(idx));
						if (imageVO != null) {
							// db에서 첨부파일 삭제
							boardImageDAO.deleteBoardImage(Integer.parseInt(idx));
							// 서버에 저장된 첨부파일 삭제
							File file = new File(path + File.separator + imageVO.getBoardImage_saveName());
							file.delete();
						}
					}
				}
			}
		}
		log.info("BoardServiceImpl-updateBoard 최종 수정완료");
	}

	@Override
	// <!-- 04. delete_글 삭제하기 -->
	// 토큰 보유시 동작.
	public void deleteBoard(int board_idx, String path, String user_id) {
		log.info("BoardServiceImpl-deleteBoard 호출 : " + board_idx + "번째 게시글, 로그인 계정 : " + user_id);
		BoardVO dbVO = null;
		UserVO userVO = null; // 현재 접속한 유저의 디비
		if (board_idx != 0) {
			dbVO = boardDAO.selectByIdx(board_idx); // 보드 디비 원본
			userVO = userDAO.selectByIdx(dbVO.getUser_idx()); // 삭제희망 게시물의 작성자 계정VO
			if (dbVO != null && userVO.getUser_id().equals(user_id)) {
				log.info("BoardServiceImpl-deleteBoard 원본 글의 작성자와 로그인 계정의 일치여부 확인");
				boardDAO.deleteBoard(dbVO.getBoard_idx());
				log.info("BoardServiceImpl-deleteBoard 게시글 삭제완료");
				if (dbVO.getBoardImageList() != null) {
					for (BoardImageVO boardImageVO : dbVO.getBoardImageList()) {
						boardImageDAO.deleteBoardImage(boardImageVO.getBoardImage_idx());
						File file = new File(path + File.separator + boardImageVO.getBoardImage_saveName());
						file.delete();
						log.info("BoardServiceImpl-deleteBoard 게시글의 BoardImage 파일 삭제완료");
					}
				}
			}
		}
		log.info("BoardServiceImpl-deleteBoard 리턴 : " + board_idx + "번 글의 최종 삭제 완료");
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
		log.info("BoardServiceImpl-selctMaxIdx 호출 ");
		int maxIdx = 0;
		maxIdx = boardDAO.selectMaxIdx();
		if (maxIdx == 0) {
			log.info("BoardServiceImpl-selctMaxIdx idx 값 0.");
			;
		}
		log.info("BoardServiceImpl-selctMaxIdx 리턴 " + maxIdx);
		return maxIdx;
	}

}
