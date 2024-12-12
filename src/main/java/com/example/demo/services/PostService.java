package com.example.demo.services;

import com.example.demo.dto.request.PostCreationRequest;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.exceptions.AppException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    public Post createPost(PostCreationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = postMapper.toPost(request, user);
        return postRepository.save(post);
    }

    // Lấy tất cả bài viết
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Lấy bài viết theo ID
    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        return post.get();
    }

    // Cập nhật bài viết
    public Post updatePost(Long id, PostCreationRequest request) {
        Post existingPost = getPostById(id);
        postMapper.updatePost(existingPost, request);
        existingPost.setUpdatedAt(LocalDate.from(LocalDateTime.now()));
        return postRepository.save(existingPost);
    }

    // Xóa bài viết
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
}