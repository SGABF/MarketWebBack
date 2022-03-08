package kr.green.sga.vo;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
댓글 테이블			
reply_idx	int			primary key	
reply_bref	int			foreign key	board에서 가져올것
reply_uref	int			foreign key	user에서 가져올것
reply_content	String	4000	not null		
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class ReplyVO {
	private int reply_idx;
	private String reply_content;
	private int board_idx;
	private int user_idx;
}
