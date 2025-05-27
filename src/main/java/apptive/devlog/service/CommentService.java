package apptive.devlog.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apptive.devlog.domain.Comment;
import apptive.devlog.domain.Post;
import apptive.devlog.domain.User;
import apptive.devlog.dto.CommentCreateDto;
import apptive.devlog.dto.CommentResponseDto;
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
    private final EmailService emailService;

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
            sendPostAuthorNotification(post);
            return true;
        }

        Comment comment = Comment.of(content, postId, user.getId());
        commentRepository.save(comment);
        sendPostAuthorNotification(post);
        return true;
    }

    private void sendPostAuthorNotification(Post post) {
        if (post == null) return;
        User author = userRepository.findById(post.getUserId()).orElse(null);
        if (author != null && !author.isDeleted()) {
            String subject = "포스팅 댓글 알림";
            String text = "회원님의 글에 새로운 댓글이 달렸습니다.";
            emailService.sendEmail(author.getEmail(), subject, text);
        }
    }

    @Transactional
    public boolean deleteComment(Long commentId, Long postId, String nickname) {
        User user = userRepository.findByNickname(nickname);
        Post post = postRepository.findById(postId).orElse(null);
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (user == null || post == null || comment == null || comment.isDeleted()) {
            return false;
        }

        if (!comment.getPostId().equals(post.getId())) {
            return false;
        }

        comment.delete();
        return true;
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComments(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.isDeleted()) { // 게시물이 없거나 삭제된 경우 빈 리스트 반환
            return Collections.emptyList();
        }

        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
            .map(comment -> {
                if (comment.isDeleted()) {
                    return new CommentResponseDto("알 수 없는 사용자", "삭제된 댓글", comment.getParentCommentId() != null);
                } else {
                    User author = userRepository.findById(comment.getUserId()).orElse(null);
                    String nickname = (author != null && !author.isDeleted()) ? author.getNickname() : "알 수 없는 사용자";
                    boolean isReply = comment.getParentCommentId() != null;
                    return new CommentResponseDto(nickname, comment.getContent(), isReply);
                }
            })
            .collect(Collectors.toList());
    }
}
