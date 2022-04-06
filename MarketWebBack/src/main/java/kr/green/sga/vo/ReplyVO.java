package kr.green.sga.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
댓글 테이블			
reply_idx		int			primary key	
reply_content	String	4000	not null		
reply_regDate	Date			
board_idx		int			foreign key	board에서 가져올것
user_idx		int			foreign key	user에서 가져올것
user_id			String		foreign key	user에서 가져올것

 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class ReplyVO {
	private int reply_idx;
	private String reply_content;
	private Date reply_regDate;
	private String reply_col3;
	private int board_idx;
	private int user_idx;
	private String user_id;
}
