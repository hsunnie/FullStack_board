package sunnies.board.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sunnies.board.exception.DataNotFoundException;
import sunnies.board.image.entity.ProfileImage;
import sunnies.board.user.entity.LocalUser;
import sunnies.board.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(String username, String password, String nickname) {
        LocalUser user = new LocalUser();
        user.setUsername(username);

        // 암호화
        user.setPassword(passwordEncoder.encode(password));

        user.setNickname(nickname);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void validateUser(String username, String nickname) {
        Optional<LocalUser> ou = userRepository.findByUsernameOrNickname(username, nickname);
        if (ou.isPresent()) {
            throw new DataIntegrityViolationException("중복된 유저입니다.");
        }
    }

    public LocalUser getUser(String username) {
        Optional<LocalUser> ou = userRepository.findByUsername(username);
        if (ou.isPresent()) {
            return ou.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public LocalUser getUser(int userId) {
        Optional<LocalUser> ou = userRepository.findById(userId);
        if (ou.isPresent()) {
            return ou.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public void updateProfile(LocalUser user, ProfileImage profileImage) {
        user.setProfileImage(profileImage);
        userRepository.save(user);
    }
}