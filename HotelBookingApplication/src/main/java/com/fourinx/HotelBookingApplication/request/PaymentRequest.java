package com.fourinx.HotelBookingApplication.request;

import lombok.Data;

@Data
public class PaymentRequest {
	private String merchantId;
	private String apiKey;
	private String bookingId;
	private GuestDetails guestDetails;
	private RoomDetails roomDetails;
	private PaymentDetails totalAmount;

}