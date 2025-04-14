package apptive.devlog.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import apptive.devlog.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse rsp, FilterChain chain)
        throws ServletException, IOException {

        String auth = req.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, rsp);
            return;
        }

        String token = auth.split(" ")[1]; // `Bearer wehjfowagehlasg` 형태의 토큰에서 공백 뒤의 토큰만 분리

        if (jwtUtil.isExpired(token)) {
            chain.doFilter(req, rsp);
            return;
        }

        String nickname = jwtUtil.getNickname(token);
        String role = jwtUtil.getRole(token);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            nickname, // principal (여기선 이메일을 사용)
            null, // credentials는 보통 null로 처리
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role)) // role을 GrantedAuthority로 wrapping!
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(req, rsp);
    }
}
