package com.fourinx.HotelBookingApplication.service;

import java.util.List;

import com.fourinx.HotelBookingApplication.model.BookedRoom;



public interface IBookingService {
	boolean cancelBooking(Long bookingId);

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);
}
