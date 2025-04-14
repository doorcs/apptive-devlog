package apptive.devlog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninDto {

    private String email;

    private String password;
} // 객체 속성들을 private로 둬 놓고, Getter Setter를 모두 사용하는 방식이 뭔가 잘못된 것 같다. 좋은 방법이 없을까?
// + DTO에는 record를 사용하는게 좋다고 하던데, 이것도 공부해보기
