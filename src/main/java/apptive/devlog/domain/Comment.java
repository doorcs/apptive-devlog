package apptive.devlog.domain;

import java.time.LocalDateTime;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId; // nullable

    @Column
    private LocalDateTime deletedAt;

    public static Comment of(String content, Long postId, Long userId, @Nullable Long parentCommentId) {
        Comment comment = new Comment();
        comment.content = content;
        comment.postId = postId;
        comment.userId = userId;
        comment.parentCommentId = parentCommentId;
        return comment;
    }

    public static Comment of(String content, Long postId, Long userId) {
        return of(content, postId, userId, null);
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
