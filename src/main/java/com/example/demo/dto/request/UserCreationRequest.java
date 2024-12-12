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
public class UserCreationRequest {
    @Size(min = 8, max = 20, message = "USERNAME_INVALID")
    String username;
    String email;
    String firstName;
    String lastName;
    LocalDate dob;
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    String password;
    String role = "USER";
}
