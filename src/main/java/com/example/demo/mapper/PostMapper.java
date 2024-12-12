package com.example.demo.mapper;

import com.example.demo.dto.request.PostCreationRequest;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    // Chuyển từ PostCreationRequest sang Post entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", source = "user") // Map user explicitly
    Post toPost(PostCreationRequest request, User user);

    // Cập nhật thông tin bài viết từ PostCreationRequest
    void updatePost(@MappingTarget Post post, PostCreationRequest request);
}
