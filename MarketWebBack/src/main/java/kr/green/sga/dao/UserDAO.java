package kr.green.sga.dao;

import java.util.HashMap;

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

	// <!-- 7. select_아디 중복 확인(0:없음/사용가능 1:있음/사용불가)-->
	int selectCountUserId(String userid);
	
	// <!-- 8. select_이름과 전화번호로 가져오기(아디찾기 사용) -->
	UserVO selectByUsername(HashMap<String, String> map);

	// <!-- 9. select_아디와 전화번호로 가져오기(비번찾기 사용) -->
	UserVO selectByUserId(HashMap<String, String> map);

	// <!-- 10. update_유저 벤 하기 -->
	void BannedUser(HashMap<String, Integer> map);
	
	// <!-- 11. update_비밀번호 변경하기 -->
	void updatePassword(HashMap<String, String> map);

	// <!-- 12. ID로 가져오기 -->
	UserVO selectUserId(String userid);


}
