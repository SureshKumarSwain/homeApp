package com.fourinx.HotelBookingApplication.repository;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fourinx.HotelBookingApplication.model.BookedRoom;

@Repository
public interface RevenueRepository extends JpaRepository<BookedRoom, Long> {

	 @Query("SELECT NEW map(FUNCTION('DATE_FORMAT', b.createdDate, '%b %Y') AS month, SUM(b.amount) AS totalRevenue) " +
	           "FROM BookedRoom b " +
	           "WHERE b.createdDate BETWEEN :startDate AND :endDate " +
	           "GROUP BY FUNCTION('DATE_FORMAT', b.createdDate, '%b %Y') " +
	           "ORDER BY MIN(b.createdDate)")
	    List<Map<String, Object>> getRevenueByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}