package apptive.devlog.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import apptive.devlog.domain.User;
import apptive.devlog.dto.SigninDto;
import apptive.devlog.jwt.JwtUtil;
import apptive.devlog.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SigninFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public SigninFilter(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil,
        ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse rsp, FilterChain chain)
        throws ServletException, IOException {

        if (!req.getRequestURI().equals("/signin") || !req.getMethod().equals("POST")) {
            chain.doFilter(req, rsp);
            return;
        } // 로그인 요청 && POST 요청이 아닐 경우 다음 filter로 넘겨주기

        // ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(req);
        // getInputStream은 한 번만 호출할 수 있기 때문에 바디를 캐싱하는 래퍼를 만들어두지 않으면 다른 필터나 서블릿에서 읽을 수 없음
        // 하지만 지금 구현하는 필터는 로그인 URI의 POST 요청만을 처리하는 필터이기 때문에 바디 캐싱이 없어도 괜찮다!

        String body = StreamUtils.copyToString(req.getInputStream(), StandardCharsets.UTF_8);
        SigninDto signinDto = objectMapper.readValue(body, SigninDto.class);

        User user = userRepository.findByEmail(signinDto.email());
        if (user == null || !passwordEncoder.matches(signinDto.password(), user.getPassword())) {
            rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            rsp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            rsp.setCharacterEncoding("UTF-8");
            rsp.getWriter().write("로그인 실패"); // 이메일이 유효하지 않거나 비밀번호가 틀림
            return; // 필터 체인에 있는 다음 필터로 넘어가지 않고 종료
        }

        String token = jwtUtil.createJwt(user.getNickname(), user.getRole(), 86400L); // 일단 하루짜리 토큰을 발급 -> 추후 리팩토링
        rsp.setStatus(HttpServletResponse.SC_OK);
        rsp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        rsp.getWriter().write("{\"token\": \"Bearer " + token + "\"}"); // 토큰을 json 형태로 rsp에 담아 return
        return; // 다음 필터로 넘어가지 않고 종료
    }
}
