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

	@Autowired(required = false)
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public UserVO getUser(UserVO userVO) {
		log.info("UserServiceImpl-getUser 호출 : " + userVO);
		UserVO dbVO = null;
		if(userVO != null) {
			dbVO = userDAO.selectUserId(userVO.getUser_id());
		}
		return dbVO;
	}

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
	
//	원본 // 기존 비번 일치하면 수정되는 메서드
//	@Override
//	// <!-- 03. update_수정하기(회원정보수정하기) -->
//	public void updateUser(UserVO userVO) {
//		log.info("UserServiceImpl-updateUser 호출 : " + userVO);
//		UserVO dbVO = null;
//		if (userVO != null) {
//			// db에서 정보를 받아와 비번이 일치하면 회원정보 수정.
//			dbVO = userDAO.selectUserId(userVO.getUser_id());
//			String dbPassword = dbVO.getUser_password();
//			if (bCryptPasswordEncoder.matches(userVO.getUser_password(), dbPassword)) {
//				log.info("UserServiceImpl-updateUser 비번 검증 : 사용자정보 일치확인");
//				// 회원정보 수정
//				userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
//				log.info("UserServiceImpl-updateUser 비번 검증 : 업데이트 진행");
//				userDAO.updateUser(userVO);
//				// 수정된 정보를 다시 얻는다.
//				dbVO = userDAO.selectByIdx(userVO.getUser_idx());
//				log.info("UserServiceImpl-updateUser 회원정보수정완료 : " + dbVO);
//			}
//		}
//	}

	@Override
	// <!-- 03. update_수정하기(회원정보수정하기) -->
	public void updateUser(UserVO userVO) {
		log.info("UserServiceImpl-updateUser 호출 : " + userVO);
		UserVO dbVO = null;
		if (userVO != null) {
			dbVO = userDAO.selectUserId(userVO.getUser_id());
			log.info("UserServiceImpl-updateUser 사용자 정보 확인 : " + dbVO);
			// 회원정보 수정
			userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
			userDAO.updateUser(userVO);
			// 수정된 정보를 다시 얻는다.
			dbVO = userDAO.selectUserId(userVO.getUser_id());
			log.info("UserServiceImpl-updateUser 회원정보수정완료 : " + dbVO);
		}
	}


	@Override
	// <!-- 04. delete_삭제하기(회원탈퇴하기) -->
	public void deleteUser(UserVO userVO) {
		log.info("UserServiceImpl-deleteUser 호출 : " + userVO);
		// 넘겨받은 userVO가 있다면
		if (userVO != null) {
			// 회원탈퇴
			userDAO.deleteUser(userVO.getUser_idx());
			log.info("UserServiceImpl-deleteUser 회원정보삭제됨 : " + userVO);
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
	// <!-- 08. select_아디 찾기 // user_name과 user_email로 VO 가져오기 -->
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
	// <!-- 09. select_비번 찾기 // user_id, user_email, user_name로 VO 가져오기 -->
	// 54. selectByUserIdNameEmail 사용
	public int findPw(String user_id, String user_email, String user_name) {
		log.info("UserServiceImpl-findPw 호출 : " + user_id + ", " + user_email + ", " + user_name);
		int count = 0;
		if (user_id != null && user_email != null && user_name != null) {
			count = userDAO.selectCountUserIdNameEmail(user_id, user_email, user_name);
			if (count == 1) {
				log.info("UserServiceImpl-findPw 리턴 : " + count + "_회원정보 있음.");
			} else {
				log.info("UserServiceImpl-findPw 리턴 : " + count + "_회원정보 없음.");
			}
		}
		return count;
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
	// <!-- 8. select_이름과 전화번호로 VO 가져오기 -->
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
	// <!-- 50. ID로 가져오기 -->
	public UserVO selectUserId(String user_id) {
		log.info("UserServiceImpl-selectUserId 호출 : " + user_id);
		UserVO userVO = null;
		if (user_id != null) {
			userVO = userDAO.selectUserId(user_id);
			log.info("UserServiceImpl-selectUserId 리턴 : 사용자 정보 확인_" + userVO);
		}
		if (userVO == null) {
			log.info("UserServiceImpl-selectUserId 리턴 : 사용자 정보 없음_" + userVO);
		}
		return userVO;
	}

	// 임시비밀번호를 만들어주는 메서드
	public String makePassword(int length) {
		Random random = new Random();
		String password = "";
		String str = "~@!#$%^&*+-*";
		for (int i = 0; i < length; i++) {
			// case의 개수가 많을수록 나타날 확율이 높아진다.
			switch (random.nextInt(8)) { // 0(숫자), 1(영어소문자), 2(영어 대문자), 3(특수문자)
			case 0:
			case 18:
			case 19:
				password += (char) ('0' + random.nextInt(10));
				break;
			case 1:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				password += (char) ('a' + random.nextInt(26));
				break;
			case 2:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				password += (char) ('A' + random.nextInt(26));
				break;
			case 3:
				password += str.charAt(random.nextInt(str.length()));
				break;
			}
		}
		return password;
	}

	@Override // 비번이 일치하는 객체 리턴하기
	public UserVO selectByUserId(UserVO userVO) {
		log.info("UserServiceImpl-selectByUserId 호출 : " + userVO);
		UserVO dbVO = null;
		if (userVO != null) {
			dbVO = userDAO.selectUserId(userVO.getUser_id());
			String dbPassword = dbVO.getUser_password(); // 암호화된 내용을 DB에서 가져옴
			if (bCryptPasswordEncoder.matches(userVO.getUser_password(), dbPassword)) { // 암호화된 비번 일치여부 확인
				log.info("UserServiceImpl-selectByUserId 검증 : 사용자 정보 일치!!");
				return userVO; // 일치하면 vo리턴
			}
		}
		log.info("UserServiceImpl-selectByUserId 리턴 : 사용자 정보 없음.");
		return null;
	}

	@Override
	public int countCheckPassword(UserVO userVO) {
		log.info("UserServiceImpl-countCheckPassword 호출 : " + userVO);
		int count = 0;
		UserVO dbVO = null;
		if(userVO!=null) {
			dbVO = userDAO.selectUserId(userVO.getUser_id());
			log.info("UserServiceImpl-countCheckPassword 사용자 db정보확인_" + dbVO);
			if(dbVO!=null) {
				// 인풋 받은 userVO의 password는 평문이고 평문과 dbVO의 bCryptPassword를 matches 해야 한다.
				String bCryptPassword = dbVO.getUser_password(); // 암호화된 내용을 DB에서 가져옴
				if (bCryptPasswordEncoder.matches(userVO.getUser_password(), bCryptPassword)) { // 암호화된 비번 일치여부 확인				
					count = 1;
					log.info("UserServiceImpl-countCheckPassword 리턴 : count_" + count);
				} else {
					log.info("UserServiceImpl-countCheckPassword 최종 리턴 : 입력한 비밀번호로 암호화된 정보 확인하였으나, 비밀번호 불일치");
				}
			} 		
		}
		log.info("UserServiceImpl-countCheckPassword 최종 리턴 : " + count);
		return count;
	}


}
