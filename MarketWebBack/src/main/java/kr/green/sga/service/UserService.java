package kr.green.sga.service;

import kr.green.sga.vo.UserVO;

public interface UserService {
	// <!-- 01. insert_저장하기(회원가입하기) -->
	void insertUser(UserVO userVO);
	
	// <!-- 02. select_1개 얻기 -->
	UserVO selectByIdx(int user_idx);
	
	// <!-- 03. update_수정하기(회원정보수정하기) -->
	void updateUser(UserVO userVO);
	
	// <!-- 04. delete_삭제하기(회원탈퇴하기) -->
	void deleteUser(UserVO userVO);
	
// -----------------------------------------------------------------
	
	// <!-- 05. select_아디 찾기 -->
	// selectByUserNameEmail 사용			(user_name, user_email)
	String findId(String user_name, String user_email);
	
	// <!-- 06. select_비번 찾기 -->
	// selectCountUserIdNameEmail 사용		(user_id, user_email, user_name)
	int findPw(String user_id, String user_email, String user_name);
	
	// ---------------------------------------------
	
	// <!-- 07. select_아디 중복 확인(0:없음/사용가능 1:있음/사용불가) -->
	// selectCountUserId 사용	(user_id)
	int idCheck(String user_id);
	
	// <!-- 10. update_유저 벤 하기 -->
	// Hashmap 사용 			(user_banned, user_idx)
	void BannedUser(UserVO UserVO);
	
	// <!-- 11. update_비밀번호 변경하기 -->
	void updatePassword(UserVO userVO);
	
	// <!-- 12. String_임시 비밀번호 생성하기 -->
	String makePassword(int length);
	
	// ---------------------------------------------
	
	// <!-- 50. select_이름과 전화번호로 가져오기(아디찾기 사용) -->
	UserVO selectByUsername(UserVO userVO);
	
	// <!-- 51. select_아디와 전화번호로 가져오기(비번찾기 사용) -->
	UserVO selectByUserId(UserVO userVO);
	
	// <!-- 52. ID로 VO 가져오기 -->
	UserVO selectUserId(String user_id);
	
	// <!-- 55. select_countCheckPassword 비밀번호 일치 여부 확인하기 -->
	int countCheckPassword(UserVO userVO);

}
