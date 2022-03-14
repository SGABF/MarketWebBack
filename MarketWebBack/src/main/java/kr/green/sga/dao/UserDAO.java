package kr.green.sga.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.UserVO;

@Mapper
public interface UserDAO {
	// <!-- 01. insert_저장하기(회원가입하기) -->
	void insertUser(UserVO userVO);

	// <!-- 02. select_1개 얻기 -->
	UserVO selectByIdx(int user_idx);

	// <!-- 03. update_수정하기(회원정보수정하기) -->
	void updateUser(UserVO userVO);

	// <!-- 04. delete_삭제하기(회원탈퇴하기) -->
	void deleteUser(int user_idx);
	
// -----------------------------------------------------------------
	
	// <!-- 07. select_아디 중복 확인(0:없음/사용가능 1:있음/사용불가)-->
	int selectCountUserId(String user_id);
	
	// <!-- 08. select_이름과 전화번호로 가져오기(아디찾기 사용) -->
	UserVO selectByUsername(HashMap<String, String> map);

	// <!-- 09. select_아디와 전화번호로 가져오기(비번찾기 사용) -->
	UserVO selectByUserId(HashMap<String, String> map);

	// <!-- 10. update_유저 벤 하기 -->
	void BannedUser(HashMap<String, Integer> map);
	
	// <!-- 11. update_비밀번호 변경하기 -->
	void updatePassword(HashMap<String, String> map);
	
// -----------------------------------------------------------------

	// <!-- 52. ID로 VO 가져오기 -->
	UserVO selectUserId(String user_id);

	// <!-- 53. 이름, 전화번호로 VO 가져오기 -->	
	// findId
	UserVO selectByUserNameEmail(String user_name, String user_email);
	
	// <!-- 54. 아디, 전화번호로 VO 가져오기 -->	
	// findPw
	UserVO selectByUserIdNameEmail(String user_id, String user_email, String user_name);




}
