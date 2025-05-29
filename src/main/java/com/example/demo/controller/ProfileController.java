package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.User;
import com.example.demo.service.ProfileService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    // Главная страница профиля
    @GetMapping
    public String showProfile(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);

        // Получаем статистику профиля
        ProfileService.ProfileStats stats = profileService.getProfileStats(user);

        // Получаем лайкнутые курсы (последние 5)
        List<Course> likedCourses = profileService.getLikedCourses(user);
        List<Course> recentLikedCourses = likedCourses.size() > 5 ? 
            likedCourses.subList(0, 5) : likedCourses;

        // Получаем избранные курсы (последние 5)
        List<Course> favoriteCourses = profileService.getFavoriteCourses(user);
        List<Course> recentFavoriteCourses = favoriteCourses.size() > 5 ? 
            favoriteCourses.subList(0, 5) : favoriteCourses;

        // Получаем созданные курсы (если пользователь - учитель)
        List<Course> userCourses = profileService.getUserCourses(user);
        List<Course> recentUserCourses = userCourses.size() > 5 ? 
            userCourses.subList(0, 5) : userCourses;

        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("recentLikedCourses", recentLikedCourses);
        model.addAttribute("recentFavoriteCourses", recentFavoriteCourses);
        model.addAttribute("recentUserCourses", recentUserCourses);

        return "profile";
    }

    // Страница всех лайкнутых курсов
    @GetMapping("/liked")
    public String showLikedCourses(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        List<Course> likedCourses = profileService.getLikedCourses(user);

        model.addAttribute("user", user);
        model.addAttribute("courses", likedCourses);
        model.addAttribute("pageTitle", "Лайкнутые курсы");
        model.addAttribute("emptyMessage", "Вы еще не лайкнули ни одного курса");

        return "profile-courses";
    }

    // Страница всех избранных курсов
    @GetMapping("/favorites")
    public String showFavoriteCourses(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        List<Course> favoriteCourses = profileService.getFavoriteCourses(user);

        model.addAttribute("user", user);
        model.addAttribute("courses", favoriteCourses);
        model.addAttribute("pageTitle", "Избранные курсы");
        model.addAttribute("emptyMessage", "У вас нет избранных курсов");

        return "profile-courses";
    }

    // Страница созданных курсов (для учителей)
    @GetMapping("/my-courses")
    public String showMyCourses(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        List<Course> userCourses = profileService.getUserCourses(user);

        model.addAttribute("user", user);
        model.addAttribute("courses", userCourses);
        model.addAttribute("pageTitle", "Мои курсы");
        model.addAttribute("emptyMessage", "Вы еще не создали ни одного курса");

        return "profile-courses";
    }

    // AJAX endpoint для лайка курса
    @PostMapping("/toggle-like/{courseId}")
    @ResponseBody
    public String toggleLike(@PathVariable Long courseId, Authentication authentication) {
        if (authentication == null) {
            return "error";
        }

        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        
        // Здесь нужно получить курс по ID
        // Для простоты возвращаем success, но в реальном приложении нужно обработать
        return "success";
    }

    // AJAX endpoint для добавления в избранное
    @PostMapping("/toggle-favorite/{courseId}")
    @ResponseBody
    public String toggleFavorite(@PathVariable Long courseId, Authentication authentication) {
        if (authentication == null) {
            return "error";
        }

        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        
        // Здесь нужно получить курс по ID
        // Для простоты возвращаем success, но в реальном приложении нужно обработать
        return "success";
    }
} 