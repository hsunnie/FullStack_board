package sunnies.board.image.controller;

import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import sunnies.board.image.service.ImageService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{id}")
    @ResponseBody
    public Resource showImage(@PathVariable("id") Long id) throws MalformedURLException {
        String imagePath = imageService.getImagePath(id);
        return new UrlResource("file:" + imagePath);
    }
}
