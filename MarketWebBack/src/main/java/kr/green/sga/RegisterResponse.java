package kr.green.sga;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegisterResponse {
	 @ApiModelProperty(notes = "회원 고유 번호", example = "1")
	    private Long user_idx;

	    @ApiModelProperty(notes = "회원 아이디", example = "id")
	    private String user_id;

	    @ApiModelProperty(notes = "회원 이름", example = "name")
	    private String user_name;

	    @ApiModelProperty(notes = "회원 전화번호", example = "01038499269")
	    private Long user_phone;

	    @ApiModelProperty(notes = "회원 이메일", example = "example@example.com")
	    private String user_email;
	}