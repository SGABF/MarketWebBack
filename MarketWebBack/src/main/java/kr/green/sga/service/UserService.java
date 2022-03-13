package kr.green.sga.service;

import java.util.HashMap;

import kr.green.sga.vo.UserVO;

public interface UserService {
	// <!-- 1. insert_저장하기(회원가입하기) -->
	void insertUser(UserVO userVO);
	
	// <!-- 2. select_1개 얻기 -->
	UserVO selectByIdx(int user_idx);
	
	// <!-- 3. update_수정하기(회원정보수정하기) -->
	void updateUser(UserVO userVO);
	
	// <!-- 4. delete_삭제하기(회원탈퇴하기) -->
	void deleteUser(UserVO userVO);
	
	// ---------------------------------------------
	// <!-- 7. select_아디 중복 확인(0:없음/사용가능 1:있음/사용불가) -->
	// selectCountUserId 사용	(user_id)
	int idCheck(String user_id);
	
	// <!-- 8. select_아디 찾기 -->
	// selectByUsername 사용	(user_name, user_phone)
	UserVO findId(UserVO UserVO);
	
	// <!-- 9. select_비번 찾기 // ID와 전화번호로 가져오기 -->
	// selectByUserId 사용		(user_id, user_phone)
	UserVO findPw(UserVO UserVO);
	
	// <!-- 10. update_유저 벤 하기 -->
	// Hashmap 사용 			(user_banned, user_idx)
	void BannedUser(UserVO UserVO);
	
	// <!-- 11. update_비밀번호 변경하기 -->
	void updatePassword(UserVO userVO);
	
	// ---------------------------------------------
	// <!-- 12. ID로 가져오기 -->
	UserVO selectUserId(String userid);

	// <!-- 12. select_이름과 전화번호로 가져오기(아디찾기 사용) -->
	UserVO selectByUsername(UserVO userVO);
	
	// <!-- 13. select_아디와 전화번호로 가져오기(비번찾기 사용) -->
	UserVO selectByUserId(UserVO userVO);
	

}
