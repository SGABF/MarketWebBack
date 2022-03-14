package kr.green.sga.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.AdminVO;

@Mapper
public interface AdminDAO {
	AdminVO getUser();
}
