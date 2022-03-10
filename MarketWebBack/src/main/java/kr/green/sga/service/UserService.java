package kr.green.sga.service;

import kr.green.sga.vo.UserVO;

public interface UserService {
	// <!-- 1. insert_저장하기(회원가입하기) -->
	void insertUser(UserVO userVO);
	
	// <!-- 2. select_1개 얻기 -->
	// 서비스로 필요하지 않음.
	
	// <!-- 3. update_수정하기(회원정보수정하기) -->
	UserVO updateUser(UserVO userVO);
	
	// <!-- 4. delete_삭제하기(회원탈퇴하기) -->
	void deleteUser(UserVO userVO);
	
	// ---------------------------------------------
	// <!-- 7. select_아디 중복 확인(0:없음/사용가능 1:있음/사용불가) -->
	// selectCountUserId 사용	(user_id)
	int idCheck(String userid);
	
	// <!-- 8. select_아디 찾기 -->
	// selectByUsername 사용	(user_name, user_phone)
	UserVO findId(UserVO UserVO);
	
	// <!-- 9. select_비번 찾기 // ID와 전화번호로 가져오기 -->
	// selectByUserId 사용		(user_id, user_phone)
	UserVO findPw(UserVO UserVO);
	
	// <!-- 11. update_비밀번호 변경하기 -->
	void updatePassword(UserVO userVO);
}
