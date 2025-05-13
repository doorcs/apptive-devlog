package apptive.devlog.service;

import org.springframework.stereotype.Service;

import apptive.devlog.domain.Post;
import apptive.devlog.domain.User;
import apptive.devlog.dto.PostCreateDto;
import apptive.devlog.repository.PostRepository;
import apptive.devlog.repository.UserRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public boolean createPost(PostCreateDto postCreateDto, String nickname) {
        String title = postCreateDto.title();
        String content = postCreateDto.content();
        User user = userRepository.findByNickname(nickname);

        if (title == null || content == null || user == null || title.isEmpty() || content.isEmpty()) {
            return false;
        }

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        postRepository.save(post);
        return true;
    }
}
