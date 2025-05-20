package apptive.devlog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apptive.devlog.dto.CommentCreateDto;
import apptive.devlog.dto.CommentResponseDto;
import apptive.devlog.jwt.JwtUtil;
import apptive.devlog.service.CommentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<String> createComment(
        @PathVariable Long postId,
        @RequestBody CommentCreateDto commentCreateDto,
        @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.substring(7);
        String nickname = jwtUtil.getNickname(token);
        boolean success = commentService.createComment(commentCreateDto, postId, nickname);

        if (success) return ResponseEntity.ok().body("댓글 작성 성공");
        else return ResponseEntity.badRequest().body("댓글 작성 실패");
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.substring(7);
        String nickname = jwtUtil.getNickname(token);
        boolean success = commentService.deleteComment(commentId, postId, nickname);

        if (success) return ResponseEntity.ok().body("댓글 삭제 성공");
        else return ResponseEntity.badRequest().body("댓글 삭제 실패");
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(
        @PathVariable Long postId
    ) {
        List<CommentResponseDto> commentResponseDtos = commentService.getAllComments(postId);

        if (commentResponseDtos == null || commentResponseDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(commentResponseDtos);
    }
}
