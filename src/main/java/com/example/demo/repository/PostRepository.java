package com.example.demo.repository;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Tìm bài viết theo ID
    Optional<Post> findById(Long id);

    // Tìm bài viết của một người dùng theo userId
    List<Post> findByUserId(Long userId);

    // Kiểm tra xem bài viết có tồn tại theo ID hay không
    boolean existsById(Long id);

    // Lấy danh sách bài viết với số lượng bình luận lớn hơn 0
    List<Post> findByCommentsCountGreaterThan(int commentsCount);

    // Tìm bài viết theo nội dung (tìm kiếm chứa từ khóa)
    List<Post> findByContentContaining(String keyword);

    List<Post> findByUser(User user);
}
