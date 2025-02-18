package com.fourinx.HotelBookingApplication.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentScreenshotDto {
    private String bookingId;
    private String upiId;
    private String screenshotPath;
    private String status = "PENDING"; // PENDING, VERIFIED, REJECTED
    private LocalDateTime uploadedAt = LocalDateTime.now();

}
