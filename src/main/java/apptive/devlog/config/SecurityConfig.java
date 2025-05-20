package apptive.devlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import apptive.devlog.filter.JwtFilter;
import apptive.devlog.filter.SigninFilter;
import apptive.devlog.jwt.JwtUtil;
import apptive.devlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {

        JwtFilter jwtFilter = new JwtFilter(jwtUtil);
        SigninFilter signinFilter = new SigninFilter(userRepository, passwordEncoder, jwtUtil, objectMapper);

        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(signinFilter, UsernamePasswordAuthenticationFilter.class) // 기존 UsernamePassword... 필터를 대체
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/v1/auth/signup", "/api/v1/auth/signin").permitAll() // 필터를 통해 회원가입을 처리하기 때문에, POST로 제한하면 안 됨!!!
                .requestMatchers(HttpMethod.GET, "/", "/api/v1/posts").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/posts/{postId}").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/posts/{postId}").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/posts", "/api/v1/auth/{nickname}").hasRole("USER")
                .anyRequest().authenticated())
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
}
