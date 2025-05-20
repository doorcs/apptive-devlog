package apptive.devlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apptive.devlog.dto.CommentCreateDto;
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
}
