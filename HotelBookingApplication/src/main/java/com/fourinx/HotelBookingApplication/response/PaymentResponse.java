package com.fourinx.HotelBookingApplication.response;

import com.fourinx.HotelBookingApplication.request.GuestDetails;
import com.fourinx.HotelBookingApplication.request.PaymentDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
	private String status;
	private String transactionId;
	private String bookingId;
	private String message;
	private PaymentDetails paymentDetails;
	private GuestDetails guestDetails;

}