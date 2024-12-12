package com.example.demo.services;

import com.example.demo.dto.request.UserCreationRequest; // DTO chứa thông tin yêu cầu tạo người dùng
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.entity.User; // Entity đại diện cho bảng người dùng trong cơ sở dữ liệu
import com.example.demo.exceptions.AppException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository; // Repository để tương tác với cơ sở dữ liệu
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository; // Inject UserRepository vào lớp dịch vụ

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    // Phương thức tạo một người dùng mới từ UserCreationRequest
    public User createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);

        // Kiểm tra người dùng đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXIST);
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lưu đối tượng User vào cơ sở dữ liệu và trả về đối tượng đã lưu
        return userRepository.save(user);
    }

    public User updateUser(Long userId, UserUpdateRequest request) {
        // Kiểm tra người dùng có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Cập nhật thông tin người dùng
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getPassword() != null) {
            // Mã hóa mật khẩu mới trước khi lưu
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Lưu thay đổi
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        userRepository.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy thông tin người dùng theo ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
