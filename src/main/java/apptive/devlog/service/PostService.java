package apptive.devlog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import apptive.devlog.domain.Post;
import apptive.devlog.domain.User;
import apptive.devlog.dto.PostCreateDto;
import apptive.devlog.dto.PostUpdateDto;
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

    public boolean updatePost(Long postId, PostUpdateDto postUpdateDto, String nickname) {
        String title = postUpdateDto.title();
        String content = postUpdateDto.content();
        User user = userRepository.findByNickname(nickname);
        Optional<Post> currPost = postRepository.findById(postId);

        if (title == null || content == null || user == null || currPost.isEmpty() || title.isEmpty() || content.isEmpty()) {
            return false;
        }

        Post newPost = currPost.get();
        newPost.setTitle(title);
        newPost.setContent(content);
        postRepository.save(newPost);
        return true;
    }

    public boolean deletePost(Long postId, String nickname) {
        User user = userRepository.findByNickname(nickname);
        Optional<Post> currPost = postRepository.findById(postId);

        if (user == null || currPost.isEmpty()) return false;

        Post post = currPost.get();
        if (!post.getUser().getNickname().equals(user.getNickname())) return false;

        postRepository.delete(post);
        return true;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getAllPostsByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname);
        if (user == null) return null;

        return postRepository.findAllByUser(user);
    }
}
