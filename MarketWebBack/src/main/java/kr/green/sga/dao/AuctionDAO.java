package kr.green.sga.dao;

import java.util.List;

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
	int selectHighUser(int auction_idx);
	// 최고입찰자 업데이트
	void updateHighUser(AuctionVO auctionVO);
	// 1개글 가져오기
	AuctionVO selectByIdx(int board_idx);
	// 1개글 가져오기(auction_idx)
	AuctionVO selectAuctionIdx(int auction_idx);
	// 최고가 가져오기(auction_idx)
	AuctionVO getHighPrice(int auction_idx);
	// 옥션 1개 지우기
	void deleteAuction(int board_idx);
	// 옥션시작
	void startAuction(int auction_idx);
	// 옥션종료
	void endAuction(int auction_idx);
	// 가격 리셋
	void resetPrice(int board_idx);
	// 가격 리셋
	void resetHighUser(int board_idx);
	
	//-----------AuctionOrder------------//
	// 입찰 신청
	void insertOrder(OrderVO orderVO);
	// 입찰 수정
	void updateOrder(OrderVO orderVO);
	// 입찰 포기
	void giveUp(OrderVO orderVO);
	// 입찰 삭제
	void deleteOrder(int order_idx);
	// 입찰 전체 삭제
	void deleteOrderAll(int auction_idx);
	// 입찰기록 1개 가져오기
	OrderVO selectByOrder(int auction_idx);
	// 1개글 입찰기록 전체 가져오기
	List<OrderVO> selectOrderList(int auction_idx);

	
	
}
