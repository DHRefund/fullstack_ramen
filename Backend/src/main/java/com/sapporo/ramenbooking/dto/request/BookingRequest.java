package com.sapporo.ramenbooking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotBlank(message = "お客様名は必須項目です。")
    @Size(min = 2, max = 100, message = "お客様名は2文字以上100文字以内で入力してください。")
    private String customerName;

    @NotBlank(message = "電話番号は必須項目です。")
    @Pattern(
        regexp = "^0[0-9]{9}$",
        message = "電話番号の形式が正しくありません。（例：09012345678）"
    )
    private String customerPhone;

    @Email(message = "メールアドレスの形式が正しくありません。")
    private String customerEmail;

    @NotNull(message = "予約日は必須項目です。")
    @Future(message = "予約日は本日以降の日付を指定してください。")
    private LocalDate bookingDate;

    @NotBlank(message = "時間帯は必須項目です。")
    private String timeSlotId;

    @NotNull(message = "人数は必須項目です。")
    @Min(value = 1, message = "人数は1名以上で指定してください。")
    @Max(value = 8, message = "人数は最大8名までです。")
    private Integer numberOfGuests;
}