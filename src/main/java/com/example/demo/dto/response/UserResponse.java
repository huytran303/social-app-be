package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;  // Trường 'id' là khóa chính cho entity User. Nó dùng để xác định duy nhất mỗi người dùng.
    String username;  // Tên người dùng.
    String email;
    String password;  // Mật khẩu của người dùng.
    String firstName;  // Tên đầu tiên của người dùng.
    String lastName;   // Họ của người dùng.
    LocalDate dob;
}
