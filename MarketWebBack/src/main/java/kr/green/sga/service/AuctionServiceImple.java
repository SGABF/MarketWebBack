/**
 * 
 */
package kr.green.sga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.AuctionDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.OrderVO;
import kr.green.sga.vo.UserVO;

@Service("AuctcionService")
public class AuctionServiceImple implements AuctionService {

	@Autowired
	private AuctionDAO auctionDAO;

	@Autowired
	private UserDAO userDAO;

	@Override
	public void insertOrder(OrderVO orderVO, String user_id) {
		UserVO dbUserVO = userDAO.selectUserId(user_id);
		orderVO.setUser_idx(dbUserVO.getUser_idx());

	}

	@Override
	public void deleteOrder(int auctionOrder_idx) {
		if (auctionOrder_idx > 0) {
			auctionDAO.deleteOrder(auctionOrder_idx);
		}
	}
}
