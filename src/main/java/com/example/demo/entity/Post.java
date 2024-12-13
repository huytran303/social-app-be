package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Mã định danh bài viết

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Mã người dùng đã đăng bài viết (liên kết với bảng Users)

    @Column(nullable = false, length = 5000)
    private String content;  // Nội dung bài viết

    @Column(nullable = true)
    private String imageUrl;  // Đường dẫn đến ảnh trong bài viết (Tùy chọn)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // Thời gian đăng bài viết

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;  // Thời gian cập nhật bài viết

    @Column(name = "likes_count", nullable = false)
    private int likesCount = 0;  // Số lượt thích

    @Column(name = "comments_count", nullable = false)
    private int commentsCount = 0;  // Số lượng bình luận

    // Thêm các phương thức để tự động cập nhật thời gian createdAt và updatedAt
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();  // Thiết lập createdAt trước khi lưu vào cơ sở dữ liệu
        this.updatedAt = LocalDateTime.now();  // Thiết lập updatedAt trước khi lưu vào cơ sở dữ liệu
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();  // Thiết lập updatedAt mỗi khi cập nhật
    }
}
