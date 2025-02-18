package com.fourinx.HotelBookingApplication.request;

import lombok.Data;

@Data
public class PaymentDetails {
	private double amount;
	private String currency;
	private String paymentMethod;
}
