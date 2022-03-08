package kr.green.sga.vo;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
사용자 포털 유저 테이블			
user_idx	int	auto increment		primary key	
user_id	String	10	not null		
user_password	VARCHAR	100	not null		
user_name	String	50	not null		
user_birth	String	8	not null		추후 수량 체크시 사용할 컬럼
user_email	String	50	not null		
user_phone	String	11	not null		
user_insertdate	DATETIME				default now
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class UserVO {
	private int user_idx;
	private String user_id;
	private String user_password;
	private String user_name;
	private String user_birth;
	private String user_email;
	private String user_phone;
	private String user_insertdate;
	private int user_banned;
}
