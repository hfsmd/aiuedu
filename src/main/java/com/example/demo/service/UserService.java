package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Сервис для работы с пользователями
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Загрузка пользователя для Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        return user;
    }

    // Регистрация нового пользователя
    public User registerUser(String username, String email, String password, UserRole role) {
        // Проверяем, существует ли уже пользователь с таким именем
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }
}