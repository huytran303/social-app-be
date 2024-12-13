package com.example.demo.repository;

import com.example.demo.entity.Likes;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    // Tìm lượt like theo bài viết và người dùng
    Likes findByPostAndUser(Post post, User user);
    // Kiểm tra xem có bản ghi nào với postId và userId không

}

