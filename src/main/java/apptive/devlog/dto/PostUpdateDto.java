package apptive.devlog.dto;

public record PostUpdateDto(String title, String content) {
    // 지금은 PostCreateDto와 동일하지만, 추후 수정될 여지가 있으니 별도의 클래스로 분리
}
