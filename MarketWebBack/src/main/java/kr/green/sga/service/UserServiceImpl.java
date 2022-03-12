package kr.green.sga.service;

import java.util.HashMap;

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
	// <!-- 1. insert_저장하기(회원가입하기) -->
	public void insertUser(UserVO userVO) {
		log.info("UserServiceImpl-insertUser 호출 : " + userVO);
		// userVO에 넘겨 받은 값이 있다면
		if (userVO != null) {
			// userVO에 값을 넣어준다.
			userDAO.insertUser(userVO);
		}
	}

	@Override
	// <!-- 3. update_수정하기(회원정보수정하기) -->
	public void updateUser(UserVO userVO) {
		log.info("UserServiceImpl-updateUser 호출 : " + userVO);
		if (userVO != null) {
			// db에서 정보를 받아와 비번이 일치하면 회원정보 수정.
			UserVO vo = userDAO.selectByIdx(userVO.getUser_idx());
			if (vo != null) {
				String dbPassword = vo.getUser_password();
				if (bCryptPasswordEncoder.matches(userVO.getUser_password(), dbPassword)) {
					// 회원정보 수정
					userDAO.updateUser(userVO);
					// 수정된 정보를 다시 얻는다.
					vo = userDAO.selectByIdx(userVO.getUser_idx());
					log.info("UserServiceImpl-updateUser 리턴 : " + vo);
				}
			}
		}
	}

	@Override
	// <!-- 4. delete_삭제하기(회원탈퇴하기) -->
	public void deleteUser(UserVO userVO) {
		log.info("UserServiceImpl-deleteUser 호출 : " + userVO);
		// 넘겨받은 userVO가 있다면
		if (userVO != null) {
			// 아이디 정보를 담아서 vo에 넣기
			UserVO vo = userDAO.selectUserId(userVO.getUser_id());
			if (vo != null) {
				// 암호화된 내용을 db에서 가져옴
				String dbPassword = vo.getUser_password();
				if (bCryptPasswordEncoder.matches(userVO.getUser_password(), dbPassword)) {
					// 회원탈퇴
					userDAO.deleteUser(vo.getUser_idx());
				}
			}
		}
	}

	@Override
	// <!-- 7. select_아디 중복 확인(0:없음/사용가능 1:있음/사용불가) -->
	// selectCountUserId 사용
	public int idCheck(String user_id) {
		log.info("ServiceImpl_idCheck 호출 : " + user_id);
		return userDAO.selectCountUserId(user_id);
	}

	@Override
	// <!-- 8. select_아디 찾기 // ID와 전화번호로 가져오기 -->
	// selectByUsername 사용
	public UserVO findId(UserVO userVO) {
		log.info("UserServiceImpl-findId 호출 : " + userVO);
		UserVO vo = null;
		if(userVO!=null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("username", userVO.getUser_name());
			map.put("phone", userVO.getUser_phone());
			vo = userDAO.selectByUsername(map);
		}
		log.info("UserServiceImpl-findId 리턴 : " + vo);
		return vo;
	}

	@Override
	// <!-- 9. select_비번 찾기 // ID와 전화번호로 가져오기 -->
	// selectByUserId 사용
	public UserVO findPw(UserVO userVO) {
		log.info("UserServiceImpl-findPw 호출 : " + userVO);
		UserVO vo = null;
		if(userVO!=null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userid", userVO.getUser_id());
			map.put("phone", userVO.getUser_phone());
			vo = userDAO.selectByUserId(map);
		}
		log.info("UserServiceImpl-findPw 리턴 : " + vo);
		return vo;
	}

	@Override
	// <!-- 11. update_비밀번호 변경하기 -->
	public void updatePassword(UserVO userVO) {
		log.info("UserServiceImpl-updatePassword 호출 : " + userVO);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", userVO.getUser_id());	
		// password는 암호화해서 저장되어야 한다.
		String encryptPassword = bCryptPasswordEncoder.encode(userVO.getUser_password());
		map.put("password", encryptPassword);		
		userDAO.updatePassword(map);
	}

	@Override
	// <!-- 10. update_유저 벤 하기 -->
	// Hashmap 사용 			(user_banned, user_idx)
	public void BannedUser(UserVO userVO) {
		log.info("UserServiceImpl-BannedUser 호출 : " + userVO);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		if(userVO.getUser_banned()==0) {
			map.put("user_banned", 1);
			map.put("user_idx", userVO.getUser_idx());
			userDAO.BannedUser(map);
		}else {
			map.put("user_banned", 0);
			map.put("user_idx", userVO.getUser_idx());
			userDAO.BannedUser(map);
		}
	}

	@Override
	public UserVO selectByIdx(int user_idx) {
		log.info("UserServiceImpl-BannedUser 호출 : " + user_idx);
		UserVO userVO = null;
		if(user_idx!=0) {
			userVO = userDAO.selectByIdx(user_idx);
		}
		log.info("UserServiceImpl-BannedUser 리턴 : " + userVO);
		return userVO;
	}
}
