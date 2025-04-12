package apptive.devlog.dto;

import apptive.devlog.domain.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {

    private String email;

    private String password;

    private String name;

    private String nickname;

    private Gender gender;
}