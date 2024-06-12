package sunnies.board.user.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sunnies.board.image.entity.ProfileImage;
import sunnies.board.image.form.ImageForm;
import sunnies.board.image.service.ImageService;
import sunnies.board.user.entity.LocalUser;
import sunnies.board.user.form.SignupForm;
import sunnies.board.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/create")
    public String userForm(SignupForm signupForm) {
        return "user/form";
    }
    
    @PostMapping("/create")
    public String userCreate(@Valid SignupForm signupForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/form";
        }
        try {
            userService.validateUser(signupForm.getUsername(), signupForm.getNickname());
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("username", "duplicateUser", "중복된 ID/Nickname 입니다.");
            return "user/form";
        }

        if (!signupForm.getPassword1().equals(signupForm.getPassword2())) {
            bindingResult.rejectValue("password2", "differentPassword", "비밀번호가 다릅니다.");
            return "user/form";
        }
        userService.createUser(signupForm.getUsername(), signupForm.getPassword1(),signupForm.getNickname());
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/mypage")
    @PreAuthorize("isAuthenticated()") //url로 mypage 접속하는 경우에 로그인이 안되어있다면, 로그인페이지로 이동할 수 있게 넣어둔 에너테이션
    public String mypage(Model model, Principal principal) {
        LocalUser user = userService.getUser(principal.getName());
        model.addAttribute("user", user);
        return "user/mypage";
    }
    
    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modify(ImageForm imageForm) {
        return "user/modify";
    }
    
    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String update(@RequestPart("imageFile") MultipartFile imageFile, Principal principal) {
        LocalUser user = userService.getUser(principal.getName());
        
        ProfileImage profileImage = null;

        try {
            profileImage = imageService.uploadImage(imageFile);
        } catch (IOException e) {
            return "user/modify";
        } catch (Exception e) {
            return "user/modify";
        }
        userService.updateProfile(user, profileImage);
        return "redirect:/user/mypage";
    }
    
        
}