package apptive.devlog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import apptive.devlog.domain.Gender;
import apptive.devlog.domain.User;
import apptive.devlog.dto.SignupDto;
import apptive.devlog.repository.UserRepository;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    } // lombok의 `@RequiredArgsConstructor`를 사용?

    public boolean signupProcess(SignupDto signupDto) {
        String email = signupDto.email();
        String password = signupDto.password();
        String name = signupDto.name();
        String nickname = signupDto.nickname();
        Gender gender = signupDto.gender();

        Boolean isEmailDuplicated = userRepository.existsByEmail(email);
        Boolean isNicknameDuplicated = userRepository.existsByNickname(nickname);

        if (isEmailDuplicated || isNicknameDuplicated)
            return false;

        User user = new User(); // setter를 쓰지 않고 builder를 활용하는 방법 공부해보기
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setNickname(nickname);
        user.setGender(gender);
        user.setRole("USER"); // `ROLE_USER`가 아니라 `USER`로 저장해준 뒤, 처리하는 단계에서 "ROLE_"를 앞에 붙여줌

        userRepository.save(user);
        return true;
    }
}