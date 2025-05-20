package apptive.devlog.dto;

public record CommentCreateDto(String content, Long userId, Long parentCommentId) {
}
