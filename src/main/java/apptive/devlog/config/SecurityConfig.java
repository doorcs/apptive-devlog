package apptive.devlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import apptive.devlog.jwt.JwtUtil;
import apptive.devlog.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf((auth) -> auth.disable())
            .formLogin((auth) -> auth.disable())
            .httpBasic((auth) -> auth.disable())
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            // new로 매번 새로 만드는것보다 생성자 주입 방식이나 @Bean 등록 방식을 사용하는 게 더 나을 것 같은데..? 이렇게 쓰는 이유를 이해하고 싶다
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/signup", "/signin").permitAll()
                .requestMatchers("/me").hasRole("USER") // 내부적으로 `hasAuthority("ROLE_USER")`로 바뀐다!!
                .anyRequest().authenticated())
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
