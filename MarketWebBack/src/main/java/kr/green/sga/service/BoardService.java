package kr.green.sga.service;

import java.util.List;

import kr.green.sga.vo.BoardVO;

public interface BoardService {
	
	//	<!-- 01. insert_글 쓰기 -->
	void insertBoard(BoardVO boardVO, String user_id);

	// 	<!-- 02. select_글 1개 가져오기 -->
	BoardVO selectByIdx(int board_idx);

	//	<!-- 03. update_글 수정하기 -->
	void updateBoard(BoardVO boardVO, String path, String[] delfile, String user_id);

	//	<!-- 04. delete_글 삭제하기 -->
	void deleteBoard(BoardVO boardVO, String path);
	
	//	<!-- 50. select_전체 개수얻기 -->
	int selectCount();

	//	<!-- 51. select_한페이지 글 목록 가져오기 -->
	List<BoardVO> selectList();

	// 	<!-- 52. 마지막에 저장한 글의 idx를 읽어오는 쿼리 -->
	int selectMaxIdx();
	
	// 	<!-- 53. 마지막에 저장한 글의 idx를 읽어오는 쿼리 -->
	List<BoardVO> selectDescLimit();

}
