package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Главная страница
    @GetMapping("/")
    public String root() {
        return "index";
    }

    // Форма входа
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Форма регистрации
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Обработка регистрации
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            Model model) {
        try {
            userService.registerUser(username, email, password, UserRole.valueOf(role.toUpperCase()));
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            model.addAttribute("user", new User());
            return "register";
        }
    }
}
