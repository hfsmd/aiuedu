package com.example.demo.repository;

import com.example.demo.model.Like;
import com.example.demo.model.User;
import com.example.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // Найти лайк пользователя для конкретного курса
    Optional<Like> findByUserAndCourse(User user, Course course);
    
    // Найти все лайки пользователя
    List<Like> findByUserOrderByCreatedAtDesc(User user);
    
    // Найти все лайки для курса
    List<Like> findByCourseOrderByCreatedAtDesc(Course course);
    
    // Подсчитать количество лайков для курса
    long countByCourse(Course course);
    
    // Проверить, лайкнул ли пользователь курс
    boolean existsByUserAndCourse(User user, Course course);
    
    // Найти курсы, которые лайкнул пользователь
    @Query("SELECT l.course FROM Like l WHERE l.user = :user ORDER BY l.createdAt DESC")
    List<Course> findLikedCoursesByUser(@Param("user") User user);
} 