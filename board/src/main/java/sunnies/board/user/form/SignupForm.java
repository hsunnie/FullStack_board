package sunnies.board.user.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupForm {
    @NotEmpty(message = "아이디는 입력해주세요.")
    @Size(min = 4, max = 16)
    private String username;
    
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    private String password2;

    @NotEmpty(message = "닉네임은 입력해주세요.")
    private String nickname;
}