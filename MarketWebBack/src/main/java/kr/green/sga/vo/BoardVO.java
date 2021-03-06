package kr.green.sga.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
판매글/구매글/경매글			
create table board(
   board_idx 			int auto_increment primary key,
   board_name 			varchar(10) not null,
   board_content 		varchar(100) not null,
   board_price 			int not null,
   board_soldout 		int not null,			0, 판매중 1, 예약중 2, 판매완료
   board_hit 			int default 0,			추후 수량 체크시 사용할 컬럼
   board_sell_category 	int not null,			0:판매글 1:구매글 2:경매글
   board_regDate 		Datetime default now(),	
   board_category 		int not null,			남성패션, 여성패션, 전자기기
   board_auctionOnOff	int default 0,			0:경매 비활성화/1:경매 활성화
   board_profile 		varchar(100) not null,  보드 대표 이미지
   boardCol2 varchar(50),
   boardCol3 varchar(50),
   user_idx int,
   foreign key (user_idx) references user(user_idx)
);
 */

@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@Data
public class BoardVO {
	private int board_idx;
	private String board_name;
	private String board_content;
	private int board_price;
	private int board_soldout;
	private int board_hit;
	private int board_sell_category;
	private Date board_regDate;
	private int board_category;
	private int board_auctionOnOff;
	private String board_profile;
	private String boardCol2;
	private String boardCol3;
	private int user_idx;
	// 판매자 아이디
	private String user_id;
	// 첨부파일을 담을 컬럼.
	private List<BoardImageVO> boardImageList;
	// 댓글을 담을 컬럼.
	private List<ReplyVO> replyList;
	// 검색 필터
	private String type;
	private String keyword;
	//----옥션컬럼(가져오기)-----//
	private AuctionVO auctionVO;
	private String highUser_id;
	
}
