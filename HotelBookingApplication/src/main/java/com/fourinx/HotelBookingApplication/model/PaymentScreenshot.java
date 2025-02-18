package com.fourinx.HotelBookingApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Table(name = "payment_screenshots")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScreenshot {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private String bookingId;

    @Column(name = "upi_id", nullable = false)
    private String upiId;

    @Column(name = "screenshot_path", nullable = false)
    private String screenshotPath;

    @Column(name = "status", nullable = false)
    private String status = "PENDING"; // PENDING, VERIFIED, REJECTED

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

}
