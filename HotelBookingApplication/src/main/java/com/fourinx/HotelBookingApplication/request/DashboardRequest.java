package com.fourinx.HotelBookingApplication.request;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data

@Getter
@Setter
@NoArgsConstructor
public class DashboardRequest {
	private LocalDate startDate;
	private LocalDate endDate;

}