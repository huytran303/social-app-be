package com.example.demo.Controller;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.exceptions.AppException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.services.AuthenticationServices;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    final AuthenticationServices authenticationServices;

    // Endpoint để xử lý đăng nhập và tạo token
    @PostMapping("/token")
    APIResponse<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request,
            HttpServletResponse response) {
        // Xử lý logic xác thực và tạo token
        var result = authenticationServices.authenticated(request);

        // Tạo cookie để lưu token
        Cookie cookie = new Cookie("token", result.getToken());
        cookie.setHttpOnly(true); // Cookie chỉ truy cập qua HTTP
        cookie.setSecure(true);  // Chỉ gửi cookie qua HTTPS (bảo mật hơn)
        cookie.setPath("/");     // Cookie áp dụng trên toàn bộ domain
        cookie.setMaxAge(60*60); // Cookie tồn tại trong 1 giờ

        // Thêm cookie vào phản hồi
        response.addCookie(cookie);

        // Trả về APIResponse với thông tin token (tuỳ chọn)
        return APIResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    // Endpoint để kiểm tra token (đọc từ cookie và kiểm tra tính hợp lệ)
    @GetMapping("/introspect")
    public APIResponse<IntrospectResponse> introspect(HttpServletRequest request) throws ParseException, JOSEException {
        // Lấy token từ cookie
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Xử lý introspect token
        var result = authenticationServices.introspect(new IntrospectRequest(token));

        // Trả về APIResponse với kết quả introspect
        return APIResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}