package apptive.devlog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apptive.devlog.domain.Comment;
import apptive.devlog.domain.Post;
import apptive.devlog.domain.User;
import apptive.devlog.dto.CommentCreateDto;
import apptive.devlog.repository.CommentRepository;
import apptive.devlog.repository.PostRepository;
import apptive.devlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean createComment(CommentCreateDto commentCreateDto, Long postId, String nickname) {
        User user = userRepository.findByNickname(nickname);
        Post post = postRepository.findById(postId).orElse(null);
        String content = commentCreateDto.content();
        Long parentCommentId = commentCreateDto.parentCommentId();

        if (content == null || user == null || post == null || content.isEmpty()) {
            return false;
        }

        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId).orElse(null);
            if (parentComment == null || parentComment.isDeleted()) {
                return false;
            }
            Comment comment = Comment.of(content, postId, user.getId(), parentCommentId);
            commentRepository.save(comment);
            return true;
        }

        Comment comment = Comment.of(content, postId, user.getId());
        commentRepository.save(comment);
        return true;
    }
}
