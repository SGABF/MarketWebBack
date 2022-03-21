package kr.green.sga.service;

import java.util.List;

import kr.green.sga.vo.BoardImageVO;

public interface BoardImageService {

	// <!-- 01. insert_첨부이미지 저장하기 -->
	void insertBoardImage(BoardImageVO boardImageVO);
	
	// <!-- 02. select_첨부 이미지 1개 가져오기 -->
	BoardImageVO selectByIdx(int boardImage_idx);
	
	// <!-- 00. update_글 수정하기 -->
	// 이미지는 수정할 일이 없다. 다시 삭제 하고 다시 올리면 된다. 
	
	// <!-- 03. delete_첨부 이미지 삭제하기 -->
	void deleteBoardImage(int boardImage_idx);

	// --------------------------------------------------
	
	// <!-- 04. 해당 board_idx의 첨부 이미지 가져오기 --> 
	List<BoardImageVO> selectByRef(int board_idx);
	
	// <!-- 05.	전체 첨부파일 가져오기 -->
	List<BoardImageVO> selectList();
	
}
