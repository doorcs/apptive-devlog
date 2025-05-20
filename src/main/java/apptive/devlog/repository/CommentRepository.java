package apptive.devlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apptive.devlog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
