package apptive.devlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import apptive.devlog.dto.SignupDto;
import apptive.devlog.service.SignupService;

@RestController
public class SignupController {

    private final SignupService signupService;

    // @Autowired 어노테이션을 통해 필드를 주입받을 수도 있지만, 생성자 주입 방식이 더 권장됨
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    } // lombok의 `@RequiredArgsConstructor` 사용?

    @PostMapping("/signup")
    public ResponseEntity<String> SignupProcess(SignupDto signupDto) {
        boolean success = signupService.signupProcess(signupDto);

        if (success) return ResponseEntity.ok().body("회원가입 성공");
        else return ResponseEntity.badRequest().body("회원가입 실패");
    }
}