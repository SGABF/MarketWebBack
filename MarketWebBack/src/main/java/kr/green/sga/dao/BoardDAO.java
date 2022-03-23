package kr.green.sga.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.BoardVO;

@Mapper
public interface BoardDAO {

	// <!-- 01. insert_글 쓰기 -->
	void insertBoard(BoardVO boardVO);
	
	// <!-- 02. select_글 1개 가져오기 -->
	BoardVO selectByIdx(int board_idx);
	
	// <!-- 03. update_글 수정하기 -->
	void updateBoard(BoardVO boardVO);
	
	// <!-- 04. delete_글 삭제하기 -->
	void deleteBoard(int board_idx);

// =======================================
	
	// <!-- 50. select_전체 개수얻기 -->
	int selectCount();
	
	// <!-- 51. 전체 글 목록 가져오기 -->
	List<BoardVO> selectList();
	
	// 	<!-- 52. 마지막에 저장한 글의 idx를 읽어오는 쿼리 -->
	int selectMaxIdx();
	
	// <!-- 53. 마지막에 저장한 글의 idx를 읽어오는 쿼리 -->
	List<BoardVO> selectDescLimit();
	
}