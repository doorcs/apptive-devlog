package apptive.devlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apptive.devlog.dto.SignupDto;
import apptive.devlog.jwt.JwtUtil;
import apptive.devlog.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> SignupProcess(@RequestBody SignupDto signupDto) {
        boolean success = authService.signupProcess(signupDto);

        if (success) return ResponseEntity.ok().body("회원가입 성공");
        else return ResponseEntity.badRequest().body("회원가입 실패");
    }

    @DeleteMapping("/auth/{nickname}")
    public ResponseEntity<String> deleteUser(
        @PathVariable String nickname,
        @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.substring(7);
        String tokenNickname = jwtUtil.getNickname(token);
        if (!nickname.equals(tokenNickname)) return ResponseEntity.badRequest().body("회원탈퇴 실패");
        // 토큰의 닉네임과 쿼리스트링의 닉네임이 일치하는지 확인한 뒤, 삭제 로직 수행
        boolean success = authService.deleteUser(nickname);

        if (success) return ResponseEntity.ok().body("회원탈퇴 성공");
        else return ResponseEntity.badRequest().body("회원탈퇴 실패");
    }
}
