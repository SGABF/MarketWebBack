package kr.green.sga.service;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.TestVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDAO userDAO;

	@Override
	public Date selectToday() {
		log.info("{}의 selectToday 호출", this.getClass().getName());
		Date today = null;
		try {
			today = userDAO.selectToday();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		log.info("{}의 selectToday 리턴 : {}", this.getClass().getName(), today);
		return today;
	}
	
}
