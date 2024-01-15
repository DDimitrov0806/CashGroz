package CashGroz.controllers;

import CashGroz.dto.UserDto;
import CashGroz.services.UserDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping
public class AuthController {
    @Autowired
    private UserDetail userDetail;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String getRegisterView(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto) {
        userDetail.registerUser(userDto);
        return "redirect:/login?success";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}