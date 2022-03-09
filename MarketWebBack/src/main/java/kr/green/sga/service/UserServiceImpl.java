package kr.green.sga.service;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void insertUser(UserVO userVO) {
		log.info("insertUser 호출 : " + userVO);
		// userVO에 넘겨 받은 값이 있다면
		if (userVO != null) {
			// userVO에 값을 넣어준다.
			userDAO.insertUser(userVO);
		}
	}

	@Override
	public UserVO updateUser(UserVO userVO) {
		if (userVO != null) {
			// db에서 정보를 받아와 비번이 일치하면 회원정보 수정.
			UserVO vo;
			vo = userDAO.selectByIdx(userVO.getUser_idx());
			if (vo != null) {
				String dbPassword = vo.getUser_password();
				if (bCryptPasswordEncoder.matches(userVO.getUser_password(), dbPassword)) {
					// 회원정보 수정
					userDAO.updateUser(userVO);
					vo = userDAO.selectByIdx(userVO.getUser_idx());
				}
				// 수정된 정보를 다시 얻는다.
				return vo;
			}
		}
		return null;
	}

	@Override
	public void deleteUser(UserVO userVO) {
		if(userVO != null) {
			UserVO vo = userDAO.
		}
	}

}
