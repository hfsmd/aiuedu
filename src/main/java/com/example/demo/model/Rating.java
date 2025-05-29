package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // 1-5 stars
    private String comment;
    private String username;
    private LocalDateTime createdAt;

    @ManyToOne
    private Course course;

    // Constructors
    public Rating() {}

    public Rating(int rating, String comment, String username, Course course) {
        this.rating = rating;
        this.comment = comment;
        this.username = username;
        this.course = course;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
} 