package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String videoPath;
    private String videoUrl;
    private int orderNumber; // Порядок урока в курсе
    private LocalDateTime createdAt;

    @ManyToOne
    private Course course;

    // Constructors
    public Lesson() {}

    public Lesson(String title, String description, Course course, int orderNumber) {
        this.title = title;
        this.description = description;
        this.course = course;
        this.orderNumber = orderNumber;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVideoPath() { return videoPath; }
    public void setVideoPath(String videoPath) { this.videoPath = videoPath; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public int getOrderNumber() { return orderNumber; }
    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
} 