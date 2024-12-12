package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;  // Tên người dùng, duy nhất, tối đa 20 ký tự

    @Column(nullable = true, unique = true, length = 255)
    private String email;  // Email người dùng, duy nhất, tối đa 255 ký tự

    @Column(nullable = false)
    private String password;  // Mật khẩu người dùng

    @Column(nullable = false)
    private String firstName;  // Tên người dùng

    @Column(nullable = false)
    private String lastName;  // Họ người dùng

    private LocalDate dob;  // Ngày sinh của người dùng

    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;  // Đường dẫn đến ảnh đại diện

    @Column(nullable = true)
    private String bio;  // Tiểu sử ngắn gọn

    @Column(name = "followers_count", nullable = false)
    private int followersCount = 0;  // Số người theo dõi

    @Column(name = "following_count", nullable = false)
    private int followingCount = 0;  // Số người đang theo dõi

    @Column(name = "posts_count", nullable = false)
    private int postsCount = 0;  // Số bài viết của người dùng

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;  // Thời gian tạo tài khoản

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;  // Thời gian cập nhật tài khoản

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Post> posts = new ArrayList<>();

    // Thêm các phương thức để tự động cập nhật thời gian createdAt và updatedAt
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();  // Thiết lập createdAt trước khi lưu vào cơ sở dữ liệu
        this.updatedAt = LocalDate.now();  // Thiết lập updatedAt trước khi lưu vào cơ sở dữ liệu
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDate.now();  // Thiết lập updatedAt mỗi khi cập nhật
    }
}
