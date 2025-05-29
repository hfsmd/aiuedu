package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfileService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // Лайки
    public boolean toggleLike(User user, Course course) {
        if (likeRepository.existsByUserAndCourse(user, course)) {
            // Убрать лайк
            likeRepository.findByUserAndCourse(user, course)
                    .ifPresent(likeRepository::delete);
            return false;
        } else {
            // Поставить лайк
            Like like = new Like(user, course);
            likeRepository.save(like);
            return true;
        }
    }

    public boolean isLiked(User user, Course course) {
        return likeRepository.existsByUserAndCourse(user, course);
    }

    public List<Course> getLikedCourses(User user) {
        return likeRepository.findLikedCoursesByUser(user);
    }

    public long getLikesCount(Course course) {
        return likeRepository.countByCourse(course);
    }

    // Избранное
    public boolean toggleFavorite(User user, Course course) {
        if (favoriteRepository.existsByUserAndCourse(user, course)) {
            // Убрать из избранного
            favoriteRepository.findByUserAndCourse(user, course)
                    .ifPresent(favoriteRepository::delete);
            return false;
        } else {
            // Добавить в избранное
            Favorite favorite = new Favorite(user, course);
            favoriteRepository.save(favorite);
            return true;
        }
    }

    public boolean isFavorite(User user, Course course) {
        return favoriteRepository.existsByUserAndCourse(user, course);
    }

    public List<Course> getFavoriteCourses(User user) {
        return favoriteRepository.findFavoriteCoursesByUser(user);
    }

    public long getFavoritesCount(Course course) {
        return favoriteRepository.countByCourse(course);
    }

    // Курсы пользователя (если он преподаватель)
    public List<Course> getUserCourses(User user) {
        if (user.getRole() == UserRole.TEACHER) {
            // Используем поиск по username, так как в существующей системе используется createdBy
            return courseRepository.findByCreatedByOrderByCreatedAtDesc(user.getUsername());
        }
        return List.of();
    }

    // Статистика профиля
    public ProfileStats getProfileStats(User user) {
        ProfileStats stats = new ProfileStats();
        stats.setLikedCoursesCount(likeRepository.findByUserOrderByCreatedAtDesc(user).size());
        stats.setFavoriteCoursesCount(favoriteRepository.findByUserOrderByCreatedAtDesc(user).size());
        
        if (user.getRole() == UserRole.TEACHER) {
            List<Course> userCourses = courseRepository.findByCreatedByOrderByCreatedAtDesc(user.getUsername());
            stats.setCreatedCoursesCount(userCourses.size());
            
            // Подсчитать общее количество лайков на курсах пользователя
            long totalLikes = userCourses.stream()
                    .mapToLong(this::getLikesCount)
                    .sum();
            stats.setTotalLikesReceived(totalLikes);
        }
        
        return stats;
    }

    // Внутренний класс для статистики
    public static class ProfileStats {
        private int likedCoursesCount;
        private int favoriteCoursesCount;
        private int createdCoursesCount;
        private long totalLikesReceived;

        // Геттеры и сеттеры
        public int getLikedCoursesCount() { return likedCoursesCount; }
        public void setLikedCoursesCount(int likedCoursesCount) { this.likedCoursesCount = likedCoursesCount; }

        public int getFavoriteCoursesCount() { return favoriteCoursesCount; }
        public void setFavoriteCoursesCount(int favoriteCoursesCount) { this.favoriteCoursesCount = favoriteCoursesCount; }

        public int getCreatedCoursesCount() { return createdCoursesCount; }
        public void setCreatedCoursesCount(int createdCoursesCount) { this.createdCoursesCount = createdCoursesCount; }

        public long getTotalLikesReceived() { return totalLikesReceived; }
        public void setTotalLikesReceived(long totalLikesReceived) { this.totalLikesReceived = totalLikesReceived; }
    }
} 