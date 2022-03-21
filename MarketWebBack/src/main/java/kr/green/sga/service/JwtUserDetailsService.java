package kr.green.sga.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	// DB 연결 전
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		if("admin".equals(username)) {
//			return new User("admin", "$2a$10$m/enYHaLsCwH2dKMUAtQp.ksGOA6lq7Fd2pnMb4L.yT4GyeAPRPyS", new ArrayList<>());
//		} else {
//			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
//		}
//	}

	// DB 연결 후
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		AdminVO adminVO = adminDAO.getUser();
//
//		if (adminVO.getAdmin_id().equals(username)) {
//			return new User(adminVO.getAdmin_id(), "$2a$10$m/enYHaLsCwH2dKMUAtQp.ksGOA6lq7Fd2pnMb4L.yT4GyeAPRPyS",
//					new ArrayList<>());
//		} else {
//			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
//		}
//	}

	// DB 연결 후
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVO dbVO = null;
		if (username != null) {
			dbVO = userService.selectUserId(username);
			if (dbVO.getUser_banned() != 0) {
				log.info("JwtUserDetailsService-loadUserByUsername 호출 : 정지된 계정의 로그인 시도 \n");
				throw new UsernameNotFoundException("정지된 계정의 로그인 시도 : " + username);
			} else {
				if (dbVO.getUser_id().equals(username)) {
					// if()
					log.info("JwtUserDetailsService-loadUserByUsername 호출 : 로그인 성공 \n");
					return new User(dbVO.getUser_id(), dbVO.getUser_password(), new ArrayList<>());
				} else {
					log.info("JwtUserDetailsService-loadUserByUsername 호출 : 사용자 못찾음 \n");
					throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
				}
			}
		}
		return null;
	}
	
	// DB 연결 후
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		AdminVO adminVO = adminDAO.getUser();
//		
//		if(adminVO.getAdmin_id().equals(username)) {
//			return new User(adminVO.getAdmin_id(), "$2a$10$m/enYHaLsCwH2dKMUAtQp.ksGOA6lq7Fd2pnMb4L.yT4GyeAPRPyS", new ArrayList<>());
//		} else {
//			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
//		}
//	}

}
