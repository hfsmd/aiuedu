package com.example.demo.repository;

import com.example.demo.model.CourseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseViewRepository extends JpaRepository<CourseView, Long> {
    
    @Query("SELECT COUNT(DISTINCT cv.ipAddress) FROM CourseView cv WHERE cv.course.id = :courseId")
    Long getUniqueViewsByCourseId(@Param("courseId") Long courseId);
    
    boolean existsByCourseIdAndUsernameAndIpAddress(Long courseId, String username, String ipAddress);
} 