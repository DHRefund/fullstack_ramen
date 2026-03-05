package com.sapporo.ramenbooking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelBookingRequest {

    @NotBlank(message = "予約IDは必須項目です。")
    private String bookingId;

    @NotBlank(message = "電話番号は必須項目です。")
    @Pattern(regexp = "^0[0-9]{9}$", message = "電話番号の形式が正しくありません。（例：09012345678）")
    private String customerPhone;

    private String cancelReason;
}