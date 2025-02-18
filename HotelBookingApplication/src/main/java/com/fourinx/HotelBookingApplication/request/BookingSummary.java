package com.fourinx.HotelBookingApplication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSummary {
	private int newBookings;
	private int checkIns;
	private int checkOuts;

}
