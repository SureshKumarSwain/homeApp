package com.fourinx.HotelBookingApplication.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fourinx.HotelBookingApplication.model.BookedRoom;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

	List<BookedRoom> findByRoomId(Long roomId);

	BookedRoom findByBookingId(Long bookingId);

	Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

	List<BookedRoom> findByGuestEmail(String email);

	 @Query("SELECT b FROM BookedRoom b WHERE b.checkInDate >= :startDate AND b.checkOutDate <= :endDate")
	    List<BookedRoom> findBookingsWithinExactRange(@Param("startDate") LocalDate startDate,  
	                                                  @Param("endDate") LocalDate endDate);
	}