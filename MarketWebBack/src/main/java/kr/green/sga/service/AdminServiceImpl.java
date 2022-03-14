package kr.green.sga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.AdminDAO;
import kr.green.sga.vo.AdminVO;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminDAO adminDAO;

	@Override
	public AdminVO getUser() {
		return adminDAO.getUser();
	}
}
