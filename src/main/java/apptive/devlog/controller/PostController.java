package apptive.devlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import apptive.devlog.dto.PostCreateDto;
import apptive.devlog.jwt.JwtUtil;
import apptive.devlog.service.PostService;

@RestController
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/posts")
    public ResponseEntity<String> createPost(
        @RequestBody PostCreateDto postCreateDto,
        @RequestHeader("Authorization") String authorization
    ) {
        String token = authorization.substring(7);
        String nickname = jwtUtil.getNickname(token);
        boolean success = postService.createPost(postCreateDto, nickname);

        if (success) return ResponseEntity.ok().body("게시글 작성 성공");
        else return ResponseEntity.badRequest().body("게시글 작성 실패");
    }
}
