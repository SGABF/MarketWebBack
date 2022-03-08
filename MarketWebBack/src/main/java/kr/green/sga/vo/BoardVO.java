package kr.green.sga.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
판매글/구매글				
board_idx		int	auto increment		primary key	
user_idx		ref	10	not null		판매자 유저 아이디
board_name		String	10	not null		
board_price		int		not null		
board_soldout	int		not null		1: 판매중/ 2: 예약중/ 3: 판매완료
board_hit		int	-			추후 수량 체크시 사용할 컬럼
board_sell_category	int	-	not null		판매글, 구매글, 경매글
board_regDate	date	-	default now		
board_category	int	-	not null		판매글, 구매글, 경매글
board_image		String				상품이미지
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class BoardVO {
	private int board_idx;
	private String board_name;
	private int board_price;
	private int board_soldout;
	private int board_hit;
	private int board_sell_category;
	private Date board_regDate;
	private int board_category;
	private int board_auctionOnOff;
	private String boardCol1;
	private String boardCol2;
	private String boardCol3;
	private String board_content;
	private int user_idx;
	
}
