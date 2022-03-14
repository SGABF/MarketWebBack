package kr.green.sga.service;

import java.util.HashMap;
import java.util.Random;

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
	// <!-- 01. insert_저장하기(회원가입하기) -->
	public void insertUser(UserVO userVO) {
		log.info("UserServiceImpl-insertUser 호출 : " + userVO);
		// userVO에 넘겨 받은 값이 있다면
		if (userVO != null) {
			// userVO에 값을 넣어준다.
			userDAO.insertUser(userVO);
		}
	}

	@Override
	// <!-- 02. select_1개 얻기 -->
	public UserVO selectByIdx(int user_idx) {
		log.info("UserServiceImpl-selectByIdx 호출 : " + user_idx);
		UserVO userVO = null;
		if (user_idx != 0) {
			userVO = userDAO.selectByIdx(user_idx);
		}
		log.info("UserServiceImpl-selectByIdx 리턴 : " + userVO);
		return userVO;
	}

	@Override
	// <!-- 03. update_수정하기(회원정보수정하기) -->
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
	// <!-- 04. delete_삭제하기(회원탈퇴하기) -->
	public void deleteUser(UserVO userVO) {
		log.info("UserServiceImpl-deleteUser 호출 : " + userVO);
		UserVO dbVO = null;
		// 넘겨받은 userVO가 있다면
		if (userVO != null) {
			// 아이디 정보를 담아서 vo에 넣기
			dbVO = userDAO.selectByIdx(userVO.getUser_idx());
			if (dbVO != null) {
				// 암호화된 내용을 db에서 가져옴
				String dbPassword = dbVO.getUser_password();
				if (bCryptPasswordEncoder.matches(userVO.getUser_password(), dbPassword)) {
					log.info("UserServiceImpl-deleteUser-bCryptPasswordEncoder.matches 호출 및 비번 일치확인");
					// 회원탈퇴
					userDAO.deleteUser(dbVO.getUser_idx());
					log.info("UserServiceImpl-deleteUser 회원정보삭제됨 : " + dbVO);
				}
			}
		}
	}

	@Override
	// <!-- 07. select_아디 중복 확인(0:없음/사용가능 1:있음/사용불가) -->
	// selectCountUserId 사용
	public int idCheck(String user_id) {
		int count = 0;
		if (user_id != null) {
			log.info("ServiceImpl_idCheck 호출 : " + user_id);
			count = userDAO.selectCountUserId(user_id);
			log.info("ServiceImpl_idCheck 리턴 : " + count);
		}
		return count;
	}

	@Override
	// <!-- 08. select_아디 찾기 // 이름과 이메일로 가져오기 -->
	// 53. selectByUserNameEmail 사용
	public String findId(String user_name, String user_email) {
		log.info("UserServiceImpl-findId 호출 : " + user_name + ", " + user_email);
		UserVO dbVO = null;
		String user_id = "";
		if (user_name != null && user_email != null) {
			dbVO = userDAO.selectByUserNameEmail(user_name, user_email);
			user_id = dbVO.getUser_id();
		}
		log.info("UserServiceImpl-findId 리턴 : " + user_id);
		return user_id;
	}

	@Override
	// <!-- 09. select_비번 찾기 // ID와 전화번호로 가져오기 -->
	// 54. selectByUserIdNameEmail 사용
	public String findPw(String user_id, String user_email, String user_name) {
		log.info("UserServiceImpl-findPw 호출 : " + user_id + ", " + user_email+ ", " + user_name);
		UserVO dbVO = null;
		if (user_id != null && user_email != null && user_name != null) {
			dbVO = userDAO.selectByUserIdNameEmail(user_id, user_email, user_name);
		}
		log.info("UserServiceImpl-findPw 리턴 : " + dbVO.getUser_password());
		return dbVO.getUser_password();
	}

	@Override
	// <!-- 11. update_비밀번호 변경하기 -->
	public void updatePassword(UserVO userVO) {
		log.info("UserServiceImpl-updatePassword 호출 : " + userVO);
		HashMap<String, String> map = new HashMap<String, String>();
		if (userVO != null) {
			map.put("user_id", userVO.getUser_id());
			// password는 암호화해서 저장되어야 한다.
			String encryptPassword = bCryptPasswordEncoder.encode(userVO.getUser_password());
			map.put("user_password", encryptPassword);
			userDAO.updatePassword(map);
		}
		log.info("UserServiceImpl-updatePassword 리턴 : " + map);
	}

	@Override
	// <!-- 10. update_유저 벤 하기 -->
	// Hashmap 사용 (user_banned, user_idx)
	public void BannedUser(UserVO userVO) {
		log.info("UserServiceImpl-BannedUser 호출 : " + userVO);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		if (userVO.getUser_banned() == 0) {
			map.put("user_banned", 1);
			map.put("user_idx", userVO.getUser_idx());
			userDAO.BannedUser(map);
		} else {
			map.put("user_banned", 0);
			map.put("user_idx", userVO.getUser_idx());
			userDAO.BannedUser(map);
		}
		log.info("UserServiceImpl-BannedUser 리턴 : " + map);
	}

	@Override
	// <!-- 8. select_이름과 전화번호로 가져오기(아디찾기 사용) -->
	public UserVO selectByUsername(UserVO userVO) {
		log.info("UserServiceImpl-selectByUsername 호출 : " + userVO);
		UserVO vo = null;
		if (userVO != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("user_name", userVO.getUser_name());
			map.put("user_phone", userVO.getUser_phone());
			vo = userDAO.selectByUsername(map);
		}
		log.info("UserServiceImpl-selectByUsername 리턴 : " + vo);
		return vo;
	}

	@Override
	// <!-- 09. select_아디와 전화번호로 가져오기(비번찾기 사용) -->
	public UserVO selectByUserId(UserVO userVO) {
		log.info("UserServiceImpl-selectByUserId 호출 : " + userVO);
		UserVO vo = null;
		if (userVO != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("user_name", userVO.getUser_id());
			map.put("user_phone", userVO.getUser_phone());
			vo = userDAO.selectByUserId(map);
		}
		log.info("UserServiceImpl-selectByUserId 리턴 : " + vo);
		return vo;
	}

	@Override
	// <!-- 50. ID로 가져오기 -->
	public UserVO selectUserId(String user_id) {
		log.info("UserServiceImpl-selectUserId 호출 : " + user_id);
		UserVO userVO = null;
		if (user_id != null) {
			userVO = userDAO.selectUserId(user_id);
		}
		if (userVO != null) {
			log.info("UserServiceImpl-selectUserId 리턴 : " + userVO);
		}
		return null;
	}
	
	
	// 임시비밀번호를 만들어주는 메서드
		public String makePassword(int length) {
			Random random = new Random();
			String password="";
			String str="~@!#$%^&*+-*";
			for(int i=0;i<length;i++) {
				// case의 개수가 많을수록 나타날 확율이 높아진다.
				switch (random.nextInt(8)) { // 0(숫자), 1(영어소문자), 2(영어 대문자), 3(특수문자)
				case 0: case 18: case 19:
					password += (char)('0' + random.nextInt(10));
					break;
				case 1: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
					password += (char)('a' + random.nextInt(26));
					break;
				case 2: case 11: case 12: case 13: case 14: case 15: case 16: case 17:
					password += (char)('A' + random.nextInt(26));
					break;
				case 3:
					password += str.charAt(random.nextInt(str.length()));
					break;
				}
			}
			return password;
		}
		
}
