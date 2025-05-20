package apptive.devlog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apptive.devlog.domain.Gender;
import apptive.devlog.domain.User;
import apptive.devlog.dto.SignupDto;
import apptive.devlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean signupProcess(SignupDto signupDto) {
        String email = signupDto.email();
        String password = signupDto.password();
        String name = signupDto.name();
        String nickname = signupDto.nickname();
        Gender gender = signupDto.gender();

        if (email == null || password == null || name == null || nickname == null || gender == null || email.isEmpty()
            || password.isEmpty() || name.isEmpty() || nickname.isEmpty() || gender.toString().isEmpty()) {
            return false;
        }

        Boolean isEmailDuplicated = userRepository.existsByEmail(email);
        Boolean isNicknameDuplicated = userRepository.existsByNickname(nickname);

        if (isEmailDuplicated || isNicknameDuplicated)
            return false;

        // 요구사항에 따른 비밀번호 검증 방법도 공부해보기. 아직 구현 안 함
        User user = User.of(email, passwordEncoder.encode(password), name, nickname, gender, "USER");

        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean deleteUser(String nickname) {
        User user = userRepository.findByNickname(nickname);
        if (user == null || user.isDeleted()) return false;

        user.delete();
        return true;
    }
}
