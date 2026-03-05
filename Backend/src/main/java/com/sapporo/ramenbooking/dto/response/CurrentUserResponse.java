package com.sapporo.ramenbooking.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserResponse {
    private String message;
}