package apptive.devlog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apptive.devlog.domain.Post;
import apptive.devlog.domain.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);
}
