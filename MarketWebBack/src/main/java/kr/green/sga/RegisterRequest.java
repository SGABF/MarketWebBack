package kr.green.sga;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @ApiParam(value = "회원 아이디", required = true, example = "1")
    private String id;

    @ApiParam(value = "회원 비밀번호", required = true, example = "e")
    private String password;

    @ApiParam(value = "회원 전화번호", example = "01033335555")
    private Long phone;

    @ApiParam(value = "회원 이메일", example = "example@example.com")
    private String email;
}