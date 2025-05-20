package apptive.devlog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apptive.devlog.domain.Post;
import apptive.devlog.domain.User;
import apptive.devlog.dto.PostCreateDto;
import apptive.devlog.dto.PostUpdateDto;
import apptive.devlog.repository.PostRepository;
import apptive.devlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean createPost(PostCreateDto postCreateDto, String nickname) {
        String title = postCreateDto.title();
        String content = postCreateDto.content();
        User user = userRepository.findByNickname(nickname);

        if (title == null || content == null || user == null || title.isEmpty() || content.isEmpty()) {
            return false;
        }

        Post post = Post.of(title, content, user.getId());

        postRepository.save(post);
        return true;
    }

    @Transactional
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

    @Transactional
    public boolean deletePost(Long postId, String nickname) {
        User user = userRepository.findByNickname(nickname);
        Optional<Post> currPost = postRepository.findById(postId);

        if (user == null || currPost.isEmpty()) return false;

        Post post = currPost.get();
        if (post.isDeleted() || !post.getUserId().equals(user.getId())) return false;

        post.delete();
        return true;
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        List<Post> tmp = postRepository.findAll();
        return maskDeletedPosts(tmp);
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPostsByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname);
        if (user == null || user.isDeleted()) return null;

        List<Post> tmp = postRepository.findAllByUserId(user.getId());
        if (tmp == null || tmp.isEmpty()) return null;
        return maskDeletedPosts(tmp);
    }

    private List<Post> maskDeletedPosts(List<Post> posts) {
        return posts.stream()
            .filter(post -> !post.isDeleted())
            .toList();
    }
}
