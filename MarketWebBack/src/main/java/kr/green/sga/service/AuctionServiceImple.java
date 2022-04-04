/**
 * 
 */
package kr.green.sga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdk.internal.org.jline.utils.Log;
import kr.green.sga.dao.AuctionDAO;
import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.AuctionVO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.OrderVO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service("AuctcionService")
public class AuctionServiceImple implements AuctionService {
	
	@Autowired
	private AuctionDAO auctionDAO;
	
	@Autowired
	private BoardDAO boardDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public void insertOrder(BoardVO boardVO, OrderVO orderVO, String user_id) {
		AuctionVO auctionVO = auctionDAO.selectByIdx(boardVO.getBoard_idx());
		UserVO dbUserVO = userDAO.selectUserId(user_id); 
		orderVO.setUser_idx(dbUserVO.getUser_idx());
		orderVO.setAuction_idx(auctionVO.getAuction_idx());
		auctionDAO.insertOrder(orderVO);
		auctionDAO.updatePrice(auctionVO);
	}

	@Override
	public void deleteOrder(OrderVO orderVO) {
		if(orderVO != null) {
			auctionDAO.deleteOrder(orderVO.getAuctionOrder_idx());
		}
		
	}

	@Override
	public void startAuction(BoardVO boardVO) {
		log.info("boardVO 호출 :" + boardVO.getBoard_idx());
		if(boardVO != null) {
			AuctionVO auctionVO = new AuctionVO();
			log.info("AuctionVO 호출 :" + auctionVO);
			auctionVO = auctionDAO.selectByIdx(boardVO.getBoard_idx());
			log.info("auctionVO 호출 :" + auctionVO);
			auctionDAO.startAuction(auctionVO.getAuction_idx());
		
		}
	}

	@Override
	public void endAuction(BoardVO boardVO) {
		log.info("boardVO 호출 :" + boardVO.getBoard_idx());
		if(boardVO != null) {
			AuctionVO auctionVO = new AuctionVO();
			log.info("AuctionVO 호출 :" + auctionVO);
			auctionVO = auctionDAO.selectByIdx(boardVO.getBoard_idx());
			log.info("auctionVO 호출 :" + auctionVO);
			auctionDAO.endAuction(auctionVO.getAuction_idx());
		
		}
		
	}

}
