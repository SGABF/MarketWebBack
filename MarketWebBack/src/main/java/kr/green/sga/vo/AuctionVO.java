package kr.green.sga.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
경매글							
board_idx			int	auto increment		foreign key	board 
reply_idx			int 	auto increment		foreign key	
auction_nowPrice	int 				현재가 (시작가격에서 업데이트 되면서 가격증가)
auction_maxPrice	int 				상한가
auction_startTime	int 				경매시작
auction_endTime		int 				경매종료	
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class AuctionVO {
	private int auction_idx;
	private int auction_highPrice;
	private Date auction_startTime;
	private Date auction_endTime;
	private int board_ref;
	private int reply_idx;
	private String auctionCol1;
	private String auctionCol2;
	private String auctionCol3;
	
	
	
	private List<OrderVO> orderVO;
}
