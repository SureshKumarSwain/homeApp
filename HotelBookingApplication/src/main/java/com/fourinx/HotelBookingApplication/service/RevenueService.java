package com.fourinx.HotelBookingApplication.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourinx.HotelBookingApplication.repository.RevenueRepository;

@Service
public class RevenueService {
	@Autowired
	private RevenueRepository revenueRepository;


	 public List<Map<String, Object>> getRevenueData(int months) {
	       //int months = getStartDateByRange(range);

	        // Calculate the start date (first day of N months ago)
		 LocalDate minusMonths = LocalDate.now().minusMonths(months);
	        LocalDate startDate = minusMonths.withDayOfMonth(1);

	        // Calculate the end date (last day of the current month)
	        LocalDate endDate = LocalDate.now().minusMonths(1);
	        endDate = endDate.withDayOfMonth(endDate.lengthOfMonth());
	        return revenueRepository.getRevenueByMonth(startDate, endDate);
	    }

	    private int getStartDateByRange(String range) {
	        return switch (range) {
	            case "Last 3 Months" -> 3;
	            case "Last 6 Months" -> 6;
	            case "Last Year" -> 12;
	            default -> throw new IllegalArgumentException("Invalid range: " + range);
	        };
	    }
	}