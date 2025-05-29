package com.example.demo.repository;

import com.example.demo.model.Course;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Репозиторий для курсов
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> findByKeyword(@Param("keyword") String keyword);

    List<Course> findByCategory(String category);

    List<Course> findByCreatedBy(String createdBy);
    
    // Найти курсы по учителю, отсортированные по дате создания
    List<Course> findByTeacherOrderByCreatedAtDesc(User teacher);
    
    // Найти курсы по учителю (альтернативный метод через createdBy)
    List<Course> findByCreatedByOrderByCreatedAtDesc(String createdBy);
}