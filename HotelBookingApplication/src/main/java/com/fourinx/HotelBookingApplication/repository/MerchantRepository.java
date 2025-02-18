package com.fourinx.HotelBookingApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fourinx.HotelBookingApplication.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
	Optional<Merchant> findByMerchantId(String merchantId);
}
