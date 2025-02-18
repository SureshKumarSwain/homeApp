package com.fourinx.HotelBookingApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fourinx.HotelBookingApplication.model.Billing;
public interface BillingRepository extends JpaRepository<Billing, Long> {
	
}