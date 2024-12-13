package com.example.demo.controller;

import com.example.demo.dto.request.PostCreationRequest;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.entity.Post;
import com.example.demo.exceptions.AppException;
import com.example.demo.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@Validated
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    APIResponse<Post> createPost(@RequestBody @Valid PostCreationRequest request) {
        APIResponse<Post> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(postService.createPost(request));
        response.setMessage("Post created successfully");
        return response;
    }

    @GetMapping
    APIResponse<List<Post>> getAllPosts() {
        APIResponse<List<Post>> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(postService.getAllPosts());
        response.setMessage("Posts found");
        return response;
    }

    @GetMapping("/{postId}")
    APIResponse<Post> getPost(@PathVariable Long postId) {
        APIResponse<Post> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(postService.getPostById(postId));
        response.setMessage("Post retrieved successfully");
        return response;
    }

    @PutMapping("/{postId}")
    APIResponse<Post> updatePost(@PathVariable Long postId, @RequestBody @Valid PostCreationRequest request) {
        APIResponse<Post> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(postService.updatePost(postId, request));
        response.setMessage("Post updated successfully");
        return response;
    }

    @DeleteMapping("/{postId}")
    APIResponse<String> deletePost(@PathVariable Long postId) {
        APIResponse<String> response = new APIResponse<>();
        if (postService.getPostById(postId) == null) {
            response.setCode(1005);
            response.setMessage("Post not found");
        } else {
            postService.deletePost(postId);
            response.setCode(1000);
            response.setMessage("Post deleted successfully");
        }
        return response;
    }

    @GetMapping("/user/{userId}")
    APIResponse<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        APIResponse<List<Post>> response = new APIResponse<>();
        response.setCode(1000);
        response.setResult(postService.getPostsByUserId(userId));
        response.setMessage("Posts found for the user");
        return response;
    }

    // Cập nhật lượt like (Like bài viết)
    @PutMapping("/{postId}/like")
    public APIResponse<Post> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        APIResponse<Post> response = new APIResponse<>();
        try {
            Post updatedPost = postService.likePost(postId, userId);
            response.setCode(1000);
            response.setResult(updatedPost);
            response.setMessage("Post liked successfully");
        } catch (AppException e) {
            response.setCode(e.getErrorCode().getCode());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // Hủy like bài viết (Unlike bài viết)
    @DeleteMapping("/{postId}/like")
    public APIResponse<Post> unlikePost(@PathVariable Long postId, @RequestParam Long userId) {
        APIResponse<Post> response = new APIResponse<>();
        try {
            Post updatedPost = postService.unlikePost(postId, userId);
            response.setCode(1000);
            response.setResult(updatedPost);
            response.setMessage("Post unliked successfully");
        } catch (AppException e) {
            response.setCode(e.getErrorCode().getCode());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping("/{postId}/like/status")
    public APIResponse<Boolean> checkLikeStatus(@PathVariable Long postId, @RequestParam Long userId) {
        APIResponse<Boolean> response = new APIResponse<>();
        try {
            boolean isLiked = postService.isPostLikedByUser(postId, userId);
            response.setCode(1000);
            response.setResult(isLiked);
            response.setMessage("Like status retrieved successfully");
        } catch (AppException e) {
            response.setCode(e.getErrorCode().getCode());
            response.setMessage(e.getMessage());
        }
        return response;
    }


}
