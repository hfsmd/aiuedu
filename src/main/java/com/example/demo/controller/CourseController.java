package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.Course;
import com.example.demo.model.CourseView;
import com.example.demo.model.Lesson;
import com.example.demo.model.Rating;
import com.example.demo.model.UserRole;
import com.example.demo.repository.CourseViewRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.RatingRepository;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// Контроллер для работы с курсами
@Controller
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseViewRepository courseViewRepository;

    // Список доступных категорий
    private static final List<String> CATEGORIES = Arrays.asList("Программирование", "Дизайн", "Маркетинг", "Бизнес");
    @PostMapping("/upload-video")
    public String uploadVideo(@RequestParam("file") MultipartFile file,
                              @RequestParam("courseId") Long courseId,
                              Authentication authentication) throws IOException {
        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));

        String uploadDir = "uploads/";
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        course.setVideoPath("/" + uploadDir + filename);
        courseService.save(course);

        return "redirect:/courses/" + courseId;
    }
    // Главная страница с курсами, поиском и фильтрами
    @GetMapping
    public String getAllCourses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            Model model,
            Authentication authentication) {
        List<Course> courses;
        if (keyword != null && !keyword.isEmpty()) {
            courses = courseService.searchCourses(keyword);
        } else if (category != null && !category.isEmpty()) {
            courses = courseService.filterByCategory(category);
        } else {
            courses = courseService.getAllCourses();
        }

        String username = authentication != null ? authentication.getName() : null;
        UserRole userRole = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))
                ? UserRole.TEACHER : UserRole.STUDENT;

        // Добавляем количество просмотров для каждого курса
        for (Course course : courses) {
            Long viewCount = courseViewRepository.getUniqueViewsByCourseId(course.getId());
            course.setViewCount(viewCount != null ? viewCount : 0L);
        }

        model.addAttribute("courses", courses);
        model.addAttribute("userRole", userRole);
        model.addAttribute("username", username);
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        return "index";
    }

    // Отобразить страницу курса
    @GetMapping("/{id}")
    public String getCourseById(@PathVariable Long id, Model model, Authentication authentication, HttpServletRequest request) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        String username = authentication != null ? authentication.getName() : "anonymous";
        UserRole userRole = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))
                ? UserRole.TEACHER : UserRole.STUDENT;
        
        // Отслеживаем просмотр курса
        String ipAddress = getClientIpAddress(request);
        if (!courseViewRepository.existsByCourseIdAndUsernameAndIpAddress(id, username, ipAddress)) {
            CourseView courseView = new CourseView(username, ipAddress, course);
            courseViewRepository.save(courseView);
        }
        
        // Получаем информацию о рейтингах и просмотрах
        Double averageRating = ratingRepository.getAverageRatingByCourseId(id);
        Long ratingCount = ratingRepository.getCountByCourseId(id);
        Long viewCount = courseViewRepository.getUniqueViewsByCourseId(id);
        
        model.addAttribute("course", course);
        model.addAttribute("userRole", userRole);
        model.addAttribute("username", username);
        model.addAttribute("comment", new Comment());
        model.addAttribute("canEdit", userRole == UserRole.TEACHER && course.getCreatedBy().equals(username));
        model.addAttribute("averageRating", averageRating != null ? averageRating : 0.0);
        model.addAttribute("ratingCount", ratingCount != null ? ratingCount : 0L);
        model.addAttribute("viewCount", viewCount != null ? viewCount : 0L);
        return "course";
    }

    // Получить IP адрес клиента
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0];
        }
    }

    // Отобразить форму создания курса
    @GetMapping("/new")
    public String showCreateCourseForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))) {
            return "redirect:/courses";
        }
        model.addAttribute("course", new Course());
        model.addAttribute("categories", CATEGORIES);
        return "create-course";
    }

    // Создать курс
   @PostMapping
public String createCourse(@ModelAttribute Course course,
                           @RequestParam("videoFile") MultipartFile videoFile,
                           Authentication authentication) throws IOException {
    String username = authentication.getName();

    // Сохраняем файл
    if (!videoFile.isEmpty()) {
        String uploadDir = "uploads/";
        String filename = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, videoFile.getBytes());

        // Сохраняем относительный путь в объект курса (нужно поле videoPath)
        course.setVideoPath("/" + uploadDir + filename);
    }

    UserRole userRole = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))
            ? UserRole.TEACHER : UserRole.STUDENT;
    courseService.createCourse(course, username, userRole);
    return "redirect:/courses";
}


    // Отобразить форму редактирования курса
    @GetMapping("/{id}/edit")
    public String showEditCourseForm(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))) {
            return "redirect:/courses";
        }
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        if (!course.getCreatedBy().equals(authentication.getName())) {
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        model.addAttribute("categories", CATEGORIES);
        return "edit-course";
    }

    // Обновить курс
    @PostMapping("/{id}/edit")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course, Authentication authentication) {
        String username = authentication.getName();
        UserRole userRole = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))
                ? UserRole.TEACHER : UserRole.STUDENT;
        courseService.updateCourse(id, course, username, userRole);
        return "redirect:/courses/" + id;
    }

    // Поставить лайк
    @PostMapping("/{id}/like")
    public String likeCourse(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        courseService.likeCourse(id, username);
        return "redirect:/courses/" + id;
    }

    // Добавить/удалить из избранного
    @PostMapping("/{id}/favorite")
    public String favoriteCourse(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        courseService.favoriteCourse(id, username);
        return "redirect:/courses/" + id;
    }

    // Добавить комментарий
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, @ModelAttribute Comment comment, Authentication authentication) {
        String username = authentication.getName();
        comment.setAuthor(username);
        courseService.addComment(id, comment);
        return "redirect:/courses/" + id;
    }
    
    // Добавить рейтинг
    @PostMapping("/{id}/rate")
    @ResponseBody
    public String rateCourse(@PathVariable Long id, 
                           @RequestParam int rating, 
                           @RequestParam(required = false) String comment,
                           Authentication authentication) {
        String username = authentication.getName();
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        
        // Проверяем, есть ли уже рейтинг от этого пользователя
        var existingRating = ratingRepository.findByCourseIdAndUsername(id, username);
        if (existingRating.isPresent()) {
            // Обновляем существующий рейтинг
            Rating r = existingRating.get();
            r.setRating(rating);
            r.setComment(comment);
            ratingRepository.save(r);
        } else {
            // Создаем новый рейтинг
            Rating newRating = new Rating(rating, comment, username, course);
            ratingRepository.save(newRating);
        }
        
        return "success";
    }
    
    // Добавить урок к курсу
    @PostMapping("/{id}/lessons")
    public String addLesson(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String description,
                           @RequestParam int orderNumber,
                           @RequestParam("videoFile") MultipartFile videoFile,
                           Authentication authentication) throws IOException {
        String username = authentication.getName();
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        
        // Проверяем, что пользователь - создатель курса
        if (!course.getCreatedBy().equals(username)) {
            throw new SecurityException("Только создатель курса может добавлять уроки");
        }
        
        Lesson lesson = new Lesson(title, description, course, orderNumber);
        
        // Сохраняем видео файл если загружен
        if (!videoFile.isEmpty()) {
            String uploadDir = "uploads/";
            String filename = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, videoFile.getBytes());
            lesson.setVideoPath("/" + uploadDir + filename);
        }
        
        lessonRepository.save(lesson);
        return "redirect:/courses/" + id;
    }
}