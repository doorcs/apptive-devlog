package apptive.devlog.domain;

import java.time.LocalDateTime;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Tsid
    private Long id; // TSID 사용

    @Column(unique = true, nullable = false)
    private String email; // 로그인 id, 중복X

    @Column(nullable = false)
    private String password; // 10자 이상, 특수문자 포함, 대문자와 소문자를 모두 포함

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String nickname; // 중복X

    @Column(nullable = false, columnDefinition = "VARCHAR(20)") // EnumType.String을 활용하되, DB 칼럼은 VARCHAR을 사용하도록 명시
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String role;

    @Column
    private LocalDateTime deletedAt; // nullable

    public static User of(String email, String password, String name, String nickname, Gender gender, String role) {
        User user = new User(); // protected
        user.email = email;
        user.password = password;
        user.name = name;
        user.nickname = nickname;
        user.gender = gender;
        user.role = role;
        return user;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
