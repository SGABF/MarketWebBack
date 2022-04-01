package kr.green.sga.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.BoardImageVO;

@Mapper
public interface BoardImageDAO {
	
	// <!-- 01. insert_첨부이미지 저장하기 -->
	void insertBoardImage(BoardImageVO boardImageVO);
	
	// <!-- 02. select_첨부 이미지 1개 가져오기 -->
	BoardImageVO selectByIdx(int boardImage_idx);
	
	// <!-- 00. update_글 수정하기 -->
	// 이미지는 수정할 일이 없다. 다시 삭제 하고 다시 올리면 된다. 
	
	// <!-- 03. delete_이미지 boardImage_idx 삭제하기 -->
	void deleteByIdx(int boardImage_idx);
	
	// <!-- 03. delete_board_idx -->
	void deleteByBoardIdx(int board_idx);

	// --------------------------------------------------
	
	// <!-- 04. 해당 board_idx의 첨부 이미지 가져오기 --> 
	List<BoardImageVO> selectByRef(int board_idx);
	
	// <!-- 05.	전체 첨부파일 가져오기 -->
	List<BoardImageVO> selectList();
	
	// <!-- 52. 가장 마지막 idx값 가져오기 -->
	int selectMaxIdx();
	
	// <!-- 보드 대표 이미지 가져오기 -->
	String selectProfile(int board_idx);
	
}
