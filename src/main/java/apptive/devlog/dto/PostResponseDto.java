package apptive.devlog.dto;

import apptive.devlog.domain.Post;

public record PostResponseDto(String userNickname, String title, String content) {

    public PostResponseDto(Post post) { // 닉네임, 제목, 내용만 담아 return
        this(
            post.getUser() != null ? post.getUser().getNickname() : null,
            post.getTitle(),
            post.getContent()
        );
    }
}