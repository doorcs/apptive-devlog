package apptive.devlog.domain;

import java.time.LocalDateTime;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter // 수정 기능을 세터 없이 어떻게 구현할 수 있을지 모르겠다. 일단 임시로 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Post : User = N:1

    @Column
    private LocalDateTime deletedAt; // nullable

    public static Post of(String title, String content, Long userId) {
        Post post = new Post(); // protected
        post.title = title;
        post.content = content;
        post.userId = userId;
        return post;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
