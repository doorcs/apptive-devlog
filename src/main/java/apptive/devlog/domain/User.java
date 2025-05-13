package apptive.devlog.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>(); // User : Post = 1 : N
}