package apptive.devlog.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    private final SecretKey secretKey; // JWT 라이브러리에서 SecretKey 타입의 객체를 사용하기 때문에 이런 방식이 필요함

    // @Value 어노테이션을 활용한 환경변수 분리
    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {

        this.secretKey = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    } // 생성자 주입 방식 활용

    public String getNickname(String token) {

        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("nickname", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }

    public String createJwt(String nickname, String role, Long expiredMs) {

        return Jwts.builder() // 빌더 패턴
            .claim("nickname", nickname) // key - value 데이터 추가
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis())) // 발행시점 추가
            .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료시점 추가
            .signWith(secretKey) // 암호화 진행 (암호키를 바탕으로 signature 추가)
            .compact(); // fin.
    }
}

