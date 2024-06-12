package sunnies.board.image.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class ImageForm {
    private MultipartFile file;
}
