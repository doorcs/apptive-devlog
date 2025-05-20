package apptive.devlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apptive.devlog.dto.SignupDto;
import apptive.devlog.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> SignupProcess(@RequestBody SignupDto signupDto) {
        boolean success = authService.signupProcess(signupDto);

        if (success) return ResponseEntity.ok().body("회원가입 성공");
        else return ResponseEntity.badRequest().body("회원가입 실패");
    }
}
