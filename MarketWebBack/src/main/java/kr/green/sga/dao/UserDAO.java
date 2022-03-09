package kr.green.sga.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.UserVO;

@Mapper
public interface UserDAO {
	// <!-- 1. insert_저장하기(회원가입하기) -->
	void insertUser(UserVO userVO);

	// <!-- 2. select_1개 얻기 -->
	UserVO selectByIdx(int idx);

	// <!-- 3. update_수정하기(회원정보수정하기) -->
	void updateUser(UserVO userVO);

	// <!-- 4. delete_삭제하기(회원탈퇴하기) -->
	void deleteUser(int idx);
	
	// 12. ID로 가져오기
	UserVO selectUserId(String userid);
		

}
