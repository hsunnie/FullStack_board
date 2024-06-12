package sunnies.board.image.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import sunnies.board.exception.DataNotFoundException;
import sunnies.board.image.entity.ProfileImage;
import sunnies.board.image.repository.ImageRepository;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    private final String rootPath = System.getProperty("user.dir");
    private final String imageDir = rootPath + "/image/";

    public ProfileImage uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new DataIntegrityViolationException("이미지가 존재하지 않습니다.");
        }
        // 이미지 저장 로직
        String originalName = imageFile.getOriginalFilename();
        String uuidName = UUID.randomUUID() + "." + getExtension(originalName);

        // 파일 저장
        imageFile.transferTo(new File(imageDir + uuidName));
        
        // ProfileImage 저장
        ProfileImage profileImage = new ProfileImage();
        profileImage.setOriginalName(originalName);
        profileImage.setUuidName(uuidName);
        imageRepository.save(profileImage);

        return profileImage;
    }

    private static String getExtension(String fileName) {
        int position = fileName.lastIndexOf(".");
        return fileName.substring(position+1);
    }

    public String getImagePath(Long id) {
        Optional<ProfileImage> oimage = this.imageRepository.findById(id);
        if (oimage.isPresent()) {
            return imageDir + oimage.get().getUuidName();
        }
        else {
            throw new DataNotFoundException("image not found");
        }
    }
}