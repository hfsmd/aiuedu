package com.example.demo.repository;

import com.example.demo.model.Favorite;
import com.example.demo.model.User;
import com.example.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    // Найти избранное пользователя для конкретного курса
    Optional<Favorite> findByUserAndCourse(User user, Course course);
    
    // Найти все избранные курсы пользователя
    List<Favorite> findByUserOrderByCreatedAtDesc(User user);
    
    // Проверить, добавлен ли курс в избранное пользователем
    boolean existsByUserAndCourse(User user, Course course);
    
    // Найти курсы в избранном пользователя
    @Query("SELECT f.course FROM Favorite f WHERE f.user = :user ORDER BY f.createdAt DESC")
    List<Course> findFavoriteCoursesByUser(@Param("user") User user);
    
    // Подсчитать количество добавлений в избранное для курса
    long countByCourse(Course course);
} 