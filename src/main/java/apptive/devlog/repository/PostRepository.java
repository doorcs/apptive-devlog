package apptive.devlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apptive.devlog.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
