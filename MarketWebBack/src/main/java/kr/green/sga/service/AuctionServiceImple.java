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
		if(boardVO!=null && orderVO!=null && user_id!=null) {
			orderVO.setUser_idx(dbUserVO.getUser_idx());
			orderVO.setAuction_idx(auctionVO.getAuction_idx());
			auctionDAO.insertOrder(orderVO);
		}
		auctionDAO.updatePrice(auctionVO);
	}
/*
	@Override
	public void deleteOrder(OrderVO orderVO) {
		AuctionVO auctionVO = null;
		UserVO userVO = null;
		auctionVO = auctionDAO.selectAuctionIdx(orderVO.getAuction_idx());
		if(orderVO != null) {
			log.info(" orderVO 호출 : " + orderVO);
			auctionDAO.deleteOrder(orderVO.getAuctionOrder_idx());
			log.info(" orderVO 삭제완료 : " + orderVO.getAuctionOrder_idx());
		//--------옥션테이블 최고입찰자  업데이트-------//
			
			userVO= userDAO.selectByIdx(auctionDAO.selectHighUser(auctionVO.getAuction_idx()));
			log.info(" userVO 호출 : " + userVO);
			
			// 옥션에 참여한 사람이 있다면 
			if(userVO != null) {
				userVO = userDAO.selectByIdx(userVO.getUser_idx());
				auctionVO.setAuctionCol1(userVO.getUser_id());
				auctionDAO.updatePrice(auctionVO);
				auctionDAO.updateHighUser(auctionVO);
			}else { // 옥션에 참여한 사람이 없다면 최고가격 리셋
				BoardVO boardVO = null;
				boardVO = boardDAO.selectByIdx(auctionVO.getBoard_ref());
				auctionDAO.resetPrice(boardVO.getBoard_idx());
				auctionDAO.resetHighUser(boardVO.getBoard_idx());
			}
			
		}
	}*/

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

	@Override
	public void updateOrder(OrderVO orderVO) {
		AuctionVO auctionVO = null;
		UserVO userVO = null;
		auctionVO = auctionDAO.selectAuctionIdx(orderVO.getAuction_idx());
		if(orderVO!=null) {
			auctionDAO.updateOrder(orderVO);
			
		//--------옥션테이블 최고입찰자  업데이트-------///
			int ref = auctionDAO.selectHighUser(auctionVO.getAuction_idx());
			userVO = userDAO.selectByIdx(ref);
			auctionVO.setAuctionCol1(userVO.getUser_id());
			auctionDAO.updatePrice(auctionVO);
			auctionDAO.updateHighUser(auctionVO);
		}
	}
	
	@Override
	public void giveupOrder(OrderVO orderVO) {
		AuctionVO auctionVO = null;
		UserVO userVO = null;
		if(orderVO!=null) {
			auctionDAO.giveUp(orderVO);
			auctionVO = auctionDAO.selectAuctionIdx(orderVO.getAuction_idx());
			int ref = auctionDAO.selectHighUser(auctionVO.getAuction_idx());
			userVO = userDAO.selectByIdx(ref);
			auctionVO.setAuctionCol1(userVO.getUser_id());
			auctionDAO.updatePrice(auctionVO);
			auctionDAO.updateHighUser(auctionVO);
			
			AuctionVO dbauctionVO = auctionDAO.selectAuctionIdx(orderVO.getAuction_idx());
			log.info(" auctionVO 호출 : " + auctionVO);
			//--------옥션테이블 최고입찰자  업데이트-------///
			if(dbauctionVO.getAuction_highPrice() == 0) {
				BoardVO boardVO = new BoardVO();
				boardVO = boardDAO.selectByIdx(auctionVO.getBoard_ref());
				auctionVO.setAuctionCol1("입찰자없음");
				auctionVO.setAuction_highPrice(boardVO.getBoard_price());
				auctionDAO.resetPrice(boardVO.getBoard_idx());
				auctionDAO.updateHighUser(auctionVO);
			}
		}
	}

}
