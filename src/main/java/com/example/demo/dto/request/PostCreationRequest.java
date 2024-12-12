package com.example.demo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {

    @Size(min = 1, max = 5000, message = "CONTENT_INVALID")
    String content;  // Nội dung bài viết

    String imageUrl;  // Đường dẫn đến ảnh trong bài viết (Tùy chọn)

    Long userId;  // Mã người dùng đăng bài

}
