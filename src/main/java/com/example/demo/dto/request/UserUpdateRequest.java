package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @NotBlank(message = "Current password is required")
    String currentPassword; // Thêm trường mật khẩu hiện tại

    String firstName;
    String lastName;
    String avatarUrl;
    LocalDate dob;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    String password; // Mật khẩu mới (nếu cần)
}
