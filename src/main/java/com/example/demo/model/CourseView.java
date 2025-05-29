package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CourseView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ipAddress;
    private LocalDateTime viewedAt;

    @ManyToOne
    private Course course;

    // Constructors
    public CourseView() {}

    public CourseView(String username, String ipAddress, Course course) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.course = course;
        this.viewedAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getViewedAt() { return viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
} 