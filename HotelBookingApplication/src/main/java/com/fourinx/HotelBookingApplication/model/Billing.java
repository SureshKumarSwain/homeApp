package com.fourinx.HotelBookingApplication.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "billing")
@Data
	public class Billing {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private int customerId;
	    private int roomId;
	    private long bookingId;
	    private LocalDate checkInDate;
	    private LocalDate checkOutDate;
	    private String email;
	    private String customerName;
	    private int numberOfGuests;
	    private String status;
	    private double totalAmount;

	  
	}