package kr.green.sga.vo;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
게시판이미지업로드								
boardImage_idx	int	auto increment		primary key	
board_idx	int	auto increment		foreign key	몇번글(board)의 첨부파일이냐를 저장할 원본글 번호
boardImage_saveName	String	varchar2(500)	not null		저장파일명
boardImage_oriName	String	varchar2(500)	not null		원본파일명
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class BoardImageVO {
	private int boardImage_idx;
	private String boardImage_saveName;
	private String boardImage_oriName;
	private String boardImageCol1;
	private String boardImageCol2;
	private String boardImageCol3;
	private int board_idx;
}
