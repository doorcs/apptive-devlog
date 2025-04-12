package apptive.devlog.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import apptive.devlog.domain.Gender;
import apptive.devlog.domain.User;
import apptive.devlog.dto.SignupDto;
import apptive.devlog.repository.UserRepository;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignupService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    } // lombok의 `@RequiredArgsConstructor`를 사용?

    public boolean signupProcess(SignupDto signupDto) {
        String email = signupDto.getEmail();
        String password = signupDto.getPassword();
        String name = signupDto.getName();
        String nickname = signupDto.getNickname();
        Gender gender = signupDto.getGender();

        Boolean isEmailDuplicated = userRepository.existsByEmail(email);
        Boolean isNicknameDuplicated = userRepository.existsByNickname(nickname);

        if (isEmailDuplicated || isNicknameDuplicated)
            return false;

        User user = new User(); // setter를 쓰지 않고 builder를 활용하는 방법 공부해보기
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);
        user.setNickname(nickname);
        user.setGender(gender);
        user.setRole("ROLE_USER");

        userRepository.save(user);
        return true;
    }
}