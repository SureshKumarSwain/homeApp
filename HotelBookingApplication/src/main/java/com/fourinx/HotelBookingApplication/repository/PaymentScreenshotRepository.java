package com.fourinx.HotelBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fourinx.HotelBookingApplication.model.PaymentScreenshot;

@Repository
public interface PaymentScreenshotRepository extends JpaRepository<PaymentScreenshot, Long> {
	PaymentScreenshot findByBookingId(String bookingId);

}