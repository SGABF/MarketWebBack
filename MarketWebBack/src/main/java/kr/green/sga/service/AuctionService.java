package kr.green.sga.service;

import kr.green.sga.vo.AuctionVO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.OrderVO;

public interface AuctionService {
	
	// 01. 입찰신청
	void insertOrder(BoardVO boardVO ,OrderVO orderVO, String user_id);
	// 02. 입찰취소(삭제)
	void deleteOrder(OrderVO orderVO);
	// 03. 옥션 시작
	void startAuction(BoardVO boardVO);
	// 04. 옥션 종료
	void endAuction(BoardVO boardVO);
}
