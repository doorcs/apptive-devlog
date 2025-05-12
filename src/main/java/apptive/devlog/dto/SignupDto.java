package apptive.devlog.dto;

import apptive.devlog.domain.Gender;

public record SignupDto(String email, String password, String name, String nickname, Gender gender) {
}