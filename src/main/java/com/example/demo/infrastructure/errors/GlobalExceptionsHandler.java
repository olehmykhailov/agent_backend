package com.example.demo.infrastructure.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(BaseException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }

    // 2. Ловим системные ошибки (500), чтобы не "плеваться" стек-трейсом в клиента
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEverything(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                500,
                "Произошла внутренняя ошибка сервера",
                LocalDateTime.now()
        );
        // Здесь можно добавить логгер: log.error(ex.getMessage(), ex);
        ex.printStackTrace();
        return ResponseEntity.status(500).body(error);
    }
}