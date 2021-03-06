package kr.green.sga.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.BackQnaVO;
import kr.green.sga.vo.QnaUserNameVO;


@Mapper
public interface BackQnaDAO {
	// 개수얻기 
	int selectCount();
	// 한개 글 가져오기
	BackQnaVO selectByIdx(int idx) ;
	// 한페이지 글 가져오기
	List<BackQnaVO> selectList();
	// 저장
	void insert(BackQnaVO backQnaVO) ;
	// 수정 
	void update(BackQnaVO backQnaVO) ;
	// 삭제 
	void delete(int idx) ;
	// 유저이름 가져오기
	List<QnaUserNameVO> selectUserName();
	// 저장유저 가져오기
	int selectSeq();
	// 답변완료
	void replyDone(int idx);
	// 답변미완료
	void replyNotYet(int idx);
	// 제목만 수정
	void updateName(int idx);
	// 내용만 수정
	void updateContent(int idx);
	// 받은 user_Id 로 정보 가져오기 
	int selectUserIdx(String userId);
	// 현재 게시글의 userIDx 가져오기
	int selectUserIdxBoard(int board_Idx);
	// 유저idx로 이름 가져오기
	String getUserName(int user_idx);
	
}