package com.sapporo.ramenbooking.controller;

import com.sapporo.ramenbooking.dto.request.LoginRequest;
import com.sapporo.ramenbooking.dto.request.CurrentUserRequest;
import com.sapporo.ramenbooking.dto.response.LoginResponse;
import com.sapporo.ramenbooking.dto.response.CurrentUserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if ("admin".equals(request.getUsername())
                && "admin123".equals(request.getPassword())) {

            return ResponseEntity.ok(
                    LoginResponse.builder()
                            .token("demo-token")
                            .username(request.getUsername())
                            .role("ADMIN")
                            .message("ログインが正常に完了しました。")
                            .build());
        }

        return ResponseEntity.status(401).body(
                LoginResponse.builder()
                        .message("ユーザー名またはパスワードが正しくありません。")
                        .build());
    }

    @PostMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(
            @RequestBody CurrentUserRequest request) {

        return ResponseEntity.ok(
                CurrentUserResponse.builder()
                        .message("ユーザー情報の認証が完了しました。")
                        .build());
    }
}