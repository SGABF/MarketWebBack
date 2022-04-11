package kr.green.sga.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.ReplyVO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private BoardDAO boardDAO;

	@Autowired(required = false)
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	JavaMailSender mailSender;
		
	@Value("${spring.mail.username}")
	String sendFrom;
		
	@Autowired 
	Environment env;

	@Override
	public UserVO getUser(UserVO userVO) {
		log.info("UserServiceImpl-getUser 호출 : " + userVO);
		UserVO dbVO = null;
		if (userVO != null) {
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
		UserVO dbVO = null;
		if (user_idx != 0) {
			dbVO = userDAO.selectByIdx(user_idx);
		}
		log.info("UserServiceImpl-selectByIdx 리턴 : " + dbVO);
		return dbVO;
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
	// selectByUserNameEmail 사용
	public String findId(String user_name, String user_email) {
		log.info("UserServiceImpl-findId 호출 : " + user_name + ", " + user_email);
		UserVO dbVO = null;
		String user_id = "";
		dbVO = userDAO.selectByUserNameEmail(user_name, user_email);
		if (dbVO != null) {
			user_id = dbVO.getUser_id();
			log.info("UserServiceImpl-findId 리턴 : " + user_id);
			return user_id;
		} else {
			log.info("UserServiceImpl-findId 리턴 : " + user_id + " 없는 사용자.");
			return "";
		}
	}

	@Override
	// <!-- 09. select_비번 찾기 // user_id, user_email, user_name로 VO 가져오기 -->
	// selectCountUserIdNameEmail 사용
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
		UserVO dbUserVO = null;
		if (userVO != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("user_name", userVO.getUser_name());
			map.put("user_phone", userVO.getUser_phone());
			dbUserVO = userDAO.selectByUsername(map);
		}
		log.info("UserServiceImpl-selectByUsername 리턴 : " + dbUserVO);
		return dbUserVO;
	}

	@Override
	// <!-- 50. ID로 가져오기 -->
	public UserVO selectUserId(String user_id) {
		log.info("UserServiceImpl-selectUserId 호출 : " + user_id);
		UserVO dbUserVO = null;
		if (user_id != null) {
			dbUserVO = userDAO.selectUserId(user_id);
			log.info("UserServiceImpl-selectUserId 리턴 : 사용자 정보 확인_" + dbUserVO);
		}
		if (dbUserVO == null) {
			log.info("UserServiceImpl-selectUserId 리턴 : 사용자 정보 없음_" + dbUserVO);
		}
		return dbUserVO;
	}
	
	@Override
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
	public int countCheckPassword(String user_id, String user_password) {
		log.info("UserServiceImpl-countCheckPassword 호출 : " + user_id);
		int count = 0;
		UserVO dbVO = null;
		if (user_id != null) {
			dbVO = userDAO.selectUserId(user_id);
			log.info("UserServiceImpl-countCheckPassword 사용자 db정보확인_" + dbVO);
			if (dbVO != null) {
				// 인풋 받은 userVO의 password는 평문이고 평문과 dbVO의 bCryptPassword를 matches 해야 한다.
				String bCryptPassword = dbVO.getUser_password(); // 암호화된 내용을 DB에서 가져옴
				if (bCryptPasswordEncoder.matches(user_password, bCryptPassword)) { // 암호화된 비번 일치여부 확인
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

	@Override
	public List<BoardVO> showMyMarket(int user_idx) {
		log.info("UserServiceImpl-showMyMarket 호출 : 마이페이지 내 마이마켓 리스트 호출 " + user_idx);
		if (user_idx != 0) {
			List<BoardVO> myMarket = new ArrayList<BoardVO>();
			myMarket = userDAO.showMyBoard(user_idx);
			log.info("UserServiceImpl-showMyMarket 마이마켓 리스트 리턴 : " + myMarket);
			return myMarket;
		} else {
			log.info("UserServiceImpl-showMyMarket 오류! 빈 리스트를 리턴합니다!");
			List<BoardVO> emptyList = new ArrayList<BoardVO>();
			return emptyList;
		}
	}

	@Override
	public LinkedHashSet<BoardVO> showMyGK(String user_id) {
		log.info("UserServiceImpl-showMyGK 호출 현재 로그인 계정 : " + user_id);
		if (user_id != null) {
			log.info("UserServiceImpl-showMyGK myReplyList 조회 시도");
			List<ReplyVO> myReplyList = userDAO.showMyReply(user_id);
			log.info("UserServiceImpl-showMyGK myReplyList 조회 완료 " + myReplyList);
			LinkedHashSet<BoardVO> board_idxs = new LinkedHashSet<BoardVO>();
			for (ReplyVO vo : myReplyList) {
				int board_idx = vo.getBoard_idx();
				log.info("UserServiceImpl-showMyGK myReplyList board_idx " + board_idx);
				BoardVO dbBoardVO = boardDAO.selectByIdx(board_idx);
				log.info("UserServiceImpl-showMyGK myReplyList dbBoardVO " + dbBoardVO);
				board_idxs.add(dbBoardVO);
				log.info("UserServiceImpl-showMyGK myReplyList myGKList " + board_idxs);
			}
			log.info("UserServiceImpl-showMyGK myReplyList 결과 확인" + board_idxs);
			return board_idxs;
		} else {
			log.info("UserServiceImpl-showMyGK 오류! 빈 리스트를 리턴합니다!");
			LinkedHashSet<BoardVO> emptyList = new LinkedHashSet<BoardVO>();
			return emptyList;
		}
	}

	@Override
	public boolean sendMail(UserVO userVO, String new_password) {
		log.info("UserServiceImpl-sendMail 호출 : 메일 발송 로직 시작");
		log.info("UserServiceImpl-sendMail 호출 : userVO " + userVO);
		log.info("UserServiceImpl-sendMail 호출 : new_password " + new_password);
		String sendTo = userVO.getUser_email();
		String mailTitle = "[개꿀마켓] GK Market 고객센터에서 " + userVO.getUser_name() + "님의 임시 비밀번호를 발송해드립니다.";
		String mailContent = 
				"안녕하세요 <Strong>" + userVO.getUser_name() + "</Strong>님. <br/>"
				+ "개꿀마켓 고객센터입니다. <br/>"
				+ "<br/>개꿀마켓 고객센터에서 고객님의 임시 비밀번호를 발송해드립니다. "
				+ "<br/>고객님의 임시 비밀번호는 아래와 같습니다. "
				+ "<br/>"
				+ "<br/>"
				+ "<input type='text' style='text-align: center; font-weight:bold;' value=" + new_password + ">" 
				+ "<br/>"
				+ "<br/>로그인 및 비밀번호 변경 후 서비스 이용바랍니다. "
				+ "<br/>감사합니다. "
				+ "<br/>"
				+ "<hr>"
				+ "<br/>"
				+ "<a href=\"https://github.com/SGABF/MarketWeb\">"
				+ "<img src=\"https://user-images.githubusercontent.com/94984063/155962173-e46894da-c522-4b6c-a174-24ffcdb29836.png\" width=\"368\" height=\"156\">"
				+ "</a>";

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				message.setTo(sendTo);
				message.setFrom(sendFrom); // env.getProperty("spring.mail.username")
				message.setSubject(mailTitle);
				message.setText(mailContent, true); // ture : html 형식 사용
			}
		};

		try {
			log.info("UserServiceImpl-sendMail 호출 : 메일 발송중!");
			mailSender.send(preparator);
		} catch (MailException e) {
			log.info("UserServiceImpl-sendMail 호출 : 메일 발송 최종 실패");
			return false;
		}
		log.info("UserServiceImpl-sendMail 호출 : 메일 발송 최종 성공");
		return true;
	}

}
