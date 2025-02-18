package com.fourinx.HotelBookingApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourinx.HotelBookingApplication.model.PaymentScreenshot;
import com.fourinx.HotelBookingApplication.repository.PaymentScreenshotRepository;
import com.fourinx.HotelBookingApplication.request.PaymentScreenshotDto;

@Service
public class PaymentService {

	@Autowired
	private PaymentScreenshotRepository paymentScreenshotRepository;

	public String getUpiId() {
		return "7873575859@ybl"; // Replace with your actual UPI ID
	}

	public String savePaymentScreenshot(String bookingId, String screenshotPath) {
	

		 // Create DTO object and set values
	    PaymentScreenshotDto paymentDto = new PaymentScreenshotDto();
	    paymentDto.setBookingId(bookingId);
	    paymentDto.setUpiId(getUpiId());
	    paymentDto.setScreenshotPath(screenshotPath);
	    paymentDto.setStatus("PENDING");

	    // Convert DTO to Entity
	    PaymentScreenshot paymentEntity = mapDtoToEntity(paymentDto);

	    // Save entity to the database
	    paymentScreenshotRepository.save(paymentEntity);

	    return "Payment Screenshot Uploaded Successfully!";
	}

	

	public String verifyPayment(String bookingId, boolean isVerified) {
		PaymentScreenshot payment = paymentScreenshotRepository.findByBookingId(bookingId);
		if (payment == null) {
			return "No payment record found!";
		}

		payment.setStatus(isVerified ? "VERIFIED" : "REJECTED");
		paymentScreenshotRepository.save(payment);
		return isVerified ? "Payment Verified!" : "Payment Rejected!";
	}
	
	public PaymentScreenshot mapDtoToEntity(PaymentScreenshotDto dto) {
	    PaymentScreenshot entity = new PaymentScreenshot();
	    entity.setBookingId(dto.getBookingId());
	    entity.setUpiId(dto.getUpiId());
	    entity.setScreenshotPath(dto.getScreenshotPath());
	    entity.setStatus(dto.getStatus());
	    return entity;
	}
}