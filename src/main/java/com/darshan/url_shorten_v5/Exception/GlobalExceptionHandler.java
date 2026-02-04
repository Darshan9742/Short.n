package com.darshan.url_shorten_v5.Exception;

import com.darshan.url_shorten_v5.Core.CollisionException;
import com.darshan.url_shorten_v5.Validation.InvalidUrlException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.InvalidClassException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidClassException.class)
    public ResponseEntity<ApiError> handleInvalidUrl(InvalidUrlException e) {
        return ResponseEntity
                .badRequest()
                .body(new ApiError("INVALID_URL", e.getMessage()));
    }

    @ExceptionHandler(CollisionException.class)
    public ResponseEntity<ApiError> handleCollision(CollisionException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError("HASH_COLLISION", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenric(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_ERROR", "Unexpected system error"));
    }
}
