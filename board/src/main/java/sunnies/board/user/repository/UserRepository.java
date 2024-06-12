package sunnies.board.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sunnies.board.user.entity.LocalUser;


public interface UserRepository extends JpaRepository<LocalUser, Integer> {
    public Optional<LocalUser> findByUsernameOrNickname(String username, String nickname);
    public Optional<LocalUser> findByUsername(String username);
}