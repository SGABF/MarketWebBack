package kr.green.sga.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.AdminVO;
import kr.green.sga.vo.AuctionVO;
import kr.green.sga.vo.OrderVO;
import kr.green.sga.vo.UserVO;

@Mapper
public interface AuctionDAO {
	//-----------Auction------------//
	// 옥션글 저장 기능
	void insertAuction(AuctionVO auctionVO);
	// 유저가 입찰시 최고가격 업데이트 기능
	void updatePrice(AuctionVO auctionVO);
	// 최고입찰 유저 가져오기
	int selectHighUser(int user_idx);
	// 1개글 가져오기
	AuctionVO selectByIdx(int board_idx);
	
	//-----------AuctionOrder------------//
	// 입찰 신청
	void insertOrder(OrderVO orderVO);
	// 입찰 삭제
	void deleteOrder(int order_idx);
	
	
	
}
