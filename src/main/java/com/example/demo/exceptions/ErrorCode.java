package com.example.demo.exceptions;

/**
 * Enum ErrorCode định nghĩa các mã lỗi và thông báo tương ứng.
 */
public enum ErrorCode {
    INVALID_KEY(1001, "Invalid Key"),
    USER_EXIST(1002, "User Existed!"),
    USERNAME_INVALID(1003, "Invalid Username, Min 8 characters, Max 20 characters"),
    PASSWORD_INVALID(1004, "Invalid Password, Min 8 characters, Max 20 characters"),
    USER_NOT_EXISTED(1005, "User Not Existed!"),
    POST_NOT_FOUND(1006, "Post Not Found!"),
    UNAUTHENTICATED(1006, "Unauthenticated"),;

    // Thuộc tính mã lỗi
    private final int code;

    // Thuộc tính thông báo lỗi
    private final String message;

    /**
     * Constructor cho enum ErrorCode
     * @param code Mã lỗi
     * @param message Thông báo lỗi
     */
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Lấy mã lỗi
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * Lấy thông báo lỗi
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
