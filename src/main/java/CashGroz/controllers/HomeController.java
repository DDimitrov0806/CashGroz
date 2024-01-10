package CashGroz.controllers;

import org.springframework.web.servlet.ModelAndView;

import CashGroz.dto.UserDto;
import CashGroz.services.UserDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping
public class HomeController {
    @Autowired
    private UserDetail userDetail;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public ModelAndView getLoginView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", new String());
        modelAndView.addObject("password", new String());
        modelAndView.setViewName("login");
        return modelAndView;
    }
    
    @PostMapping("/login")
    public ModelAndView authenticateUser(@RequestBody UserDto userDto) {        
        try {
            Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("index");
            return modelAndView;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    
    @GetMapping("/register")
    public ModelAndView getRegisterView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", new String());
        modelAndView.addObject("password", new String());
        modelAndView.addObject("confirmPassword", new String());
        modelAndView.setViewName("register");
        return modelAndView;
    }
    
    @PostMapping("/register")
    public ModelAndView registerUser(@RequestBody UserDto userDto) {
        try {
            userDetail.registerUser(userDto);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("username", new String());
            modelAndView.addObject("password", new String());
            modelAndView.addObject("confirmPassword", new String());
            modelAndView.setViewName("login");
            return modelAndView;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    } 

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
