package com.fourinx.HotelBookingApplication.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DashBoardResponse {

	private long bookedRooms;
	private long availableRooms;
	private int maintenanceRooms;
	private int newBookings;
	private int checkIn;
	private int checkOut;
	private double totalRevenue;
	private String date;
	private int unOccupiedRooms;
	
	private Map<String, Double> dayRevenue;

}
