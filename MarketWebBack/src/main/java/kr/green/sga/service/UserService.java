package kr.green.sga.service;

import java.util.Date;

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
	

}
