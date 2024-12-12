package com.example.demo.services;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.exceptions.AppException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServices {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signedKey}")
    protected String SIGNER_KEY;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        // Lấy token từ yêu cầu introspect
        var token = request.getToken();

        // Tạo một JWSVerifier sử dụng khóa bí mật (SIGNER_KEY) để xác minh chữ ký
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Phân tích chuỗi token thành một đối tượng SignedJWT
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Lấy thời gian hết hạn của token từ các claims
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Xác minh chữ ký của token bằng cách sử dụng JWSVerifier
        boolean verified = signedJWT.verify(verifier);

        // Kiểm tra xem token có hết hạn hay không
        boolean isNotExpired = expiryTime != null && expiryTime.after(new Date());

        // Trả về kết quả kiểm tra token qua IntrospectResponse
        return IntrospectResponse.builder()
                .valid(verified && isNotExpired) // Token hợp lệ nếu chữ ký đúng và chưa hết hạn
                .build();
    }

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!result) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user.getUsername());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(String username) {
        try {
            // Truy xuất user từ database
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            // Tạo header cho token
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            // Tạo JWTClaimsSet, thêm ID của user vào claims
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer("YourIssuer")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                    .claim("userId", user.getId()) // Thêm ID của user vào claim
                    .build();

            // Tạo JWSObject từ header và claims
            JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));

            // Ký token với SIGNER_KEY
            JWSSigner signer = new MACSigner(SIGNER_KEY.getBytes());
            jwsObject.sign(signer);

            // Trả về token đã được ký
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

}