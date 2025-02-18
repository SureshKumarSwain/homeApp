package com.fourinx.HotelBookingApplication.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class MyBookingResponse {

	private List<BookingResponse> upcomingBookings;
	private List<BookingResponse> pastBookings;
	private List<BookingResponse> cancelledBookings;


	public MyBookingResponse(List<BookingResponse> upcomingBookings, List<BookingResponse> pastBookings,
			List<BookingResponse> cancelledBookings) {
		this.upcomingBookings = upcomingBookings;
		this.pastBookings = pastBookings;
		this.cancelledBookings = cancelledBookings;
	}
}
