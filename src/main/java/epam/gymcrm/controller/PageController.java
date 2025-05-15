package epam.gymcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register/trainee")
    public String showTraineeRegisterPage() {
        return "register-trainee";
    }

    @GetMapping("/register/trainer")
    public String showTrainerRegisterPage() {
        return "register-trainer";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/login";
    }
}
