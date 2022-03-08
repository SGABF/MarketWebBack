package kr.green.sga.vo;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
댓글이미지업로드
replyImage_idx	int	auto increment		primary key	
board_idx	int	auto increment		foreign key	몇번글(board)의 첨부파일이냐를 저장할 원본글 번호
replyImage_saveName	String	varchar2(500)	not null		저장파일명
replyImage_oriName	String	varchar2(500)	not null		원본파일명
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class ReplyImageVO {
	private int replyImage_idx;
	private String replyImage_saveName;
	private String replyImage_oriName;
	private String replyImage_col1;
	private String replyImage_col2;
	private String replyImage_col3;
	private int reply_idx;
}
