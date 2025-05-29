package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.Course;
import com.example.demo.model.UserRole;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Сервис для работы с курсами
@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CommentRepository commentRepository;

    // Получить все курсы
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Получить курс по ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // Поиск по ключевому слову
    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByKeyword(keyword);
    }

    // Фильтрация по категории
    public List<Course> filterByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    // Получить курсы, созданные пользователем
    public List<Course> getCoursesByCreator(String username) {
        return courseRepository.findByCreatedBy(username);
    }

    // Создать курс
    public Course createCourse(Course course, String username, UserRole userRole) {
        if (userRole != UserRole.TEACHER) {
            throw new SecurityException("Только учитель может создавать курсы");
        }
        course.setCreatedBy(username);
        return courseRepository.save(course);
    }

    // Редактировать курс
    public Course updateCourse(Long id, Course updatedCourse, String username, UserRole userRole) {
        if (userRole != UserRole.TEACHER) {
            throw new SecurityException("Только учитель может редактировать курсы");
        }
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        if (!course.getCreatedBy().equals(username)) {
            throw new SecurityException("Вы не можете редактировать чужой курс");
        }
        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());
        course.setCategory(updatedCourse.getCategory());
        course.setVideoUrl(updatedCourse.getVideoUrl());
        return courseRepository.save(course);
    }
    public Course save(Course course) {
        return courseRepository.save(course);
    }


    // Поставить лайк
    public Course likeCourse(Long id, String username) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        course.setLikes(course.getLikes() + 1);
        return courseRepository.save(course);
    }

    // Добавить/удалить из избранного
    public Course favoriteCourse(Long id, String username) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        List<String> favoritedBy = course.getFavoritedBy();
        if (favoritedBy.contains(username)) {
            favoritedBy.remove(username);
        } else {
            favoritedBy.add(username);
        }
        return courseRepository.save(course);
    }

    // Добавить комментарий
    public Course addComment(Long id, Comment comment) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));
        
        // Устанавливаем связь с курсом
        comment.setCourse(course);
        comment.setCreatedAt(java.time.LocalDateTime.now());
        
        // Сохраняем комментарий
        Comment savedComment = commentRepository.save(comment);
        
        // Обновляем курс
        course.getComments().add(savedComment);
        return courseRepository.save(course);
    }
}