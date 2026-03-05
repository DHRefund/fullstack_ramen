package com.sapporo.ramenbooking.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserRequest {
    private String token;

}