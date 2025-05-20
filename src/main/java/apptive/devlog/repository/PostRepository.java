package apptive.devlog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apptive.devlog.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserId(Long id);
}
