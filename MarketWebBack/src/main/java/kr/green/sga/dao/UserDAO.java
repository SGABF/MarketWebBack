package kr.green.sga.dao;

import java.sql.SQLException;
import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.UserVO;

@Mapper
public interface UserDAO {
	// 
	Date selectToday() throws SQLException;
	
	// <!-- 1. insert_저장하기(회원가입하기) -->
	void insertUser() throws SQLException;
	
	// <!-- 2. select_1개 얻기 -->
	UserVO selectByIdx(int idx) throws SQLException;
	
	// <!-- 3. update_수정하기(회원정보수정하기) -->
	void updateUser() throws SQLException;
	
	// <!-- 4. delete_삭제하기(회원탈퇴하기) -->
	void deleteUser() throws SQLException;
	
	
}
