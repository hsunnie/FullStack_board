package sunnies.board.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sunnies.board.image.entity.ProfileImage;

public interface ImageRepository extends JpaRepository<ProfileImage, Long> { }