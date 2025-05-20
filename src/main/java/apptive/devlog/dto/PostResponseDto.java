package apptive.devlog.dto;

import java.util.List;

import apptive.devlog.domain.Post;

public record PostResponseDto(String title, String content) {

    public PostResponseDto(Post post) { // 닉네임, 제목, 내용만 담아 return
        this(
            post.getTitle(),
            post.getContent()
        );
    }
}