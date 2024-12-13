package com.example.demo.services;

import com.example.demo.dto.request.PostCreationRequest;
import com.example.demo.entity.Likes;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.exceptions.AppException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.LikesRepository;
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

    @Autowired
    private LikesRepository likesRepository;

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
        existingPost.setUpdatedAt(LocalDateTime.from(LocalDateTime.now()));
        return postRepository.save(existingPost);
    }

    // Xóa bài viết
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }


    public List<Post> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return postRepository.findByUser(user);
    }

    // Like bài viết
    public Post likePost(Long postId, Long userId) {
        // Tìm bài viết và người dùng
        Post post = getPostById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra xem người dùng đã like bài viết chưa
        Likes existingLike = likesRepository.findByPostAndUser(post, user);
        if (existingLike != null) {
            throw new AppException(ErrorCode.ALREADY_LIKED);  // Người dùng đã like rồi
        }

        // Lưu thông tin lượt like vào bảng Likes
        Likes like = Likes.builder()
                .post(post)
                .user(user)
                .createdAt(LocalDate.now())  // Thêm ngày giờ tạo like
                .build();
        likesRepository.save(like);  // Lưu vào bảng Likes

        // Cập nhật số lượt like của bài viết
        post.setLikesCount(post.getLikesCount() + 1);
        return postRepository.save(post);  // Lưu bài viết đã cập nhật
    }

    // Hủy like bài viết
    public Post unlikePost(Long postId, Long userId) {
        // Tìm bài viết và người dùng
        Post post = getPostById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra nếu người dùng đã like bài viết
        Likes existingLike = likesRepository.findByPostAndUser(post, user);
        if (existingLike == null) {
            throw new AppException(ErrorCode.NOT_LIKED);  // Người dùng chưa like bài viết này
        }

        // Xóa thông tin lượt like từ bảng Likes
        likesRepository.delete(existingLike);  // Xóa khỏi bảng Likes

        // Cập nhật số lượt like của bài viết
        post.setLikesCount(post.getLikesCount() - 1);
        return postRepository.save(post);  // Lưu bài viết đã cập nhật
    }

    // Kiểm tra xem bài viết đã được người dùng like chưa
    public boolean isPostLikedByUser(Long postId, Long userId) {
        // Tìm bài viết và người dùng
        Post post = getPostById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra trong bảng Likes xem người dùng đã like bài viết chưa
        Likes existingLike = likesRepository.findByPostAndUser(post, user);
        return existingLike != null;  // Nếu đã like thì trả về true, ngược lại false
    }



}