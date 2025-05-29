package com.example.demo.repository;

import com.example.demo.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findByCourseId(Long courseId);
    
    Optional<Rating> findByCourseIdAndUsername(Long courseId, String username);
    
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.course.id = :courseId")
    Double getAverageRatingByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.course.id = :courseId")
    Long getCountByCourseId(@Param("courseId") Long courseId);
} 