package apptive.devlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apptive.devlog.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);
}