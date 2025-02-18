package com.fourinx.HotelBookingApplication.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourinx.HotelBookingApplication.exception.InvalidBookingRequestException;
import com.fourinx.HotelBookingApplication.model.BookedRoom;
import com.fourinx.HotelBookingApplication.model.Room;
import com.fourinx.HotelBookingApplication.repository.BookingRepository;
import com.fourinx.HotelBookingApplication.request.DashboardRequest;
import com.fourinx.HotelBookingApplication.response.DashBoardResponse;

@Service
public class DashBoardService {

    @Autowired
    private IRoomService roomService;

    @Autowired
    private BookingRepository bookingRepository;

    public DashBoardResponse getDashboardStats(DashboardRequest request) {
        DashBoardResponse response = new DashBoardResponse();
        Date date = new Date();
        LocalDate today = LocalDate.now();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new InvalidBookingRequestException("End date must be after start date.");
        }

        // Fetch all rooms
        List<Room> allRooms = roomService.getAllRooms();
        int totalRooms = allRooms.size();
        System.out.println("Total Rooms: " + totalRooms);

		/*
		 * // Fetch bookings for the given date range List<BookedRoom> bookingList =
		 * bookingRepository.findBookingsWithinDates(startDate, endDate);
		 * System.out.println("Total Bookings Found: " + bookingList.size());
		 */
        
        List<BookedRoom> bookingList = bookingRepository.findBookingsWithinExactRange(startDate, endDate);
        System.out.println("Total Exact Match Bookings: " + bookingList.size());
     // Count new bookings
        long newBookingCount = bookingList.stream()
                .filter(booking -> !booking.getCreatedDate().isBefore(startDate) && !booking.getCreatedDate().isAfter(endDate))
                .count();
        System.out.println("New Bookings Count: " + newBookingCount);

        // Count check-ins
        long checkInCount = bookingList.stream()
                .filter(booking -> !booking.getCheckInDate().isBefore(startDate) && !booking.getCheckInDate().isAfter(endDate))
                .count();
        System.out.println("Check-In Count: " + checkInCount);

        // Count check-outs
        long checkOutCount = bookingList.stream()
                .filter(booking -> !booking.getCheckOutDate().isBefore(startDate) && !booking.getCheckOutDate().isAfter(endDate))
                .count();
        System.out.println("Check-Out Count: " + checkOutCount);

        // Calculate total revenue
        double totalRevenue = bookingList.stream()
                .mapToDouble(BookedRoom::getAmount)
                .sum();
        System.out.println("Total Revenue: " + totalRevenue);
        
       
        // Total room slots across the given days
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long totalAvailableRoomSlots = totalRooms * totalDays;
        System.out.println("Total Room Slots Available: " + totalAvailableRoomSlots);

        // Calculate unoccupied room slots
        long unOccupiedRooms = Math.max(totalAvailableRoomSlots - bookingList.size(), 0);
        System.out.println("Unoccupied Room Slots: " + unOccupiedRooms);

    
		// Calculate booked and available rooms
		int bookedRoomCount = (int) bookingList.stream().filter(booking -> today.isEqual(booking.getCheckInDate()))
				.count();

		/*
		 * int availableRoomCount = (int) allRooms.stream().filter(room ->
		 * isRoomAvailable(room, bookingList, today)) .count();
		 */

		int availableRoomCount = (int) allRooms.stream()
				.filter(room -> bookingList.stream()
						.noneMatch(booking -> booking.getRoom().equals(room) && booking.getCheckInDate().equals(today)))
				.count();

        // ✅ Set response fields
        response.setAvailableRooms(availableRoomCount);
        response.setBookedRooms(bookedRoomCount);
        response.setNewBookings((int) newBookingCount);
        response.setCheckIn((int) checkInCount);
        response.setCheckOut((int) checkOutCount);
        response.setTotalRevenue(totalRevenue);
        response.setUnOccupiedRooms((int) unOccupiedRooms);
        response.setDate(date.toString());

        return response;
    }
}
