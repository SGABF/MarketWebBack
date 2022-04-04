package kr.green.sga.service;

import kr.green.sga.vo.OrderVO;

public interface AuctionService {
	
	// 01. 입찰신청
	void insertOrder(OrderVO orderVO, String user_id);
	// 02. 입찰취소(삭제)
	void deleteOrder(int auctionOrder_idx);
	
}
