package com.fourinx.HotelBookingApplication.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourinx.HotelBookingApplication.exception.InvalidBookingRequestException;
import com.fourinx.HotelBookingApplication.exception.ResourceNotFoundException;
import com.fourinx.HotelBookingApplication.model.BookedRoom;
import com.fourinx.HotelBookingApplication.model.Room;
import com.fourinx.HotelBookingApplication.repository.BookingRepository;

@Service

public class BookingService implements IBookingService {
	  private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private IRoomService roomService;

	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}

	@Override
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}

	/*
	 * @Override public List<BookedRoom> getBookingsByUserEmail(String email) {
	 * List<BookedRoom> bookings = bookingRepository.findByGuestEmail(email);
	 * LocalDate today = LocalDate.now();
	 * 
	 * for (BookedRoom booking : bookings) { LocalDate checkInDate =
	 * booking.getCheckInDate();
	 * 
	 * // Update status based on today's date if (checkInDate.isEqual(today)) {
	 * booking.setStatus("Booked"); // Update to "Booked" if check-in is today }
	 * else if (checkInDate.isAfter(today)) { booking.setStatus("Prebooked"); //
	 * Keep "Prebooked" if it's a future booking }
	 * 
	 * bookingRepository.save(booking); // Save the updated status in DB }
	 * 
	 * return bookings; }
	 */

	/*
	 * @Override public void cancelBooking(Long bookingId) {
	 * bookingRepository.deleteById(bookingId); }
	 */

	/*
	 * @Override public void cancelBooking(Long bookingId) { Optional<BookedRoom>
	 * existingBookingOpt = bookingRepository.findById(bookingId);
	 * 
	 * if (existingBookingOpt.isPresent()) { BookedRoom existingBooking =
	 * existingBookingOpt.get();
	 * 
	 * // Example: Updating status to "Updated" (Modify this logic as needed)
	 * existingBooking.setStatus("Cancelled");
	 * 
	 * // Save the updated booking bookingRepository.save(existingBooking); } else {
	 * throw new ResourceNotFoundException("Booking not found with ID: " +
	 * bookingId); } }
	 */

	@Override
	public boolean cancelBooking(Long bookingId) {
		try {
			Optional<BookedRoom> bookingOpt = bookingRepository.findById(bookingId);

			if (bookingOpt.isEmpty()) {
				throw new ResourceNotFoundException("Booking not found with ID: " + bookingId);
			}

			BookedRoom booking = bookingOpt.get();
			LocalDate today = LocalDate.now();
			LocalTime cancellationCutoffTime = LocalTime.of(9, 0); // 9 AM

			// Check if the booking status is "Prebooked"
			if (!"Prebooked".equalsIgnoreCase(booking.getStatus())) {
				throw new IllegalStateException("Only prebooked bookings can be cancelled.");
			}

			LocalDate checkInDate = booking.getCheckInDate();
			LocalDateTime currentTime = LocalDateTime.now();

			// Allow cancellation if today is before check-in date OR if it's check-in day
			// and before cutoff time
			if (checkInDate.isAfter(today)
					|| (checkInDate.isEqual(today) && currentTime.toLocalTime().isBefore(cancellationCutoffTime))) {

				booking.setStatus("Cancelled");
				bookingRepository.save(booking);
				return true; // Return true for successful cancellation
			} else {
				throw new IllegalStateException("Cancellation not allowed after check-in time.");
			}
		} catch (ResourceNotFoundException e) {
			logger.error("Error: " + e.getMessage()); // Logging the error
			return false;
		} catch (IllegalStateException e) {
			logger.error("Error: " + e.getMessage()); // Logging the error
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error occurred: " + e.getMessage());
			return false;
		}
	}

	@Override
	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check-in date must come before check-out date");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existingBookings = room.getBookings();
		boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
		LocalDate today = LocalDate.now();
		if (roomIsAvailable) {
			room.addBooking(bookingRequest);
			// Set the room and price in BookedRoom
	        bookingRequest.setRoom(room);
	        bookingRequest.setAmount(room.getRoomPrice()); // Setting room price in BookedRoom
	        //bookingRequest.setIdProof(room.getBookings().get(0).getIdProof());
	        //bookingRequest.setPhone(room.getBookings().get(0).getPhone());
			LocalDate checkInDate = bookingRequest.getCheckInDate();

			// Update status based on today's date
			if (checkInDate.isEqual(today)) {
				bookingRequest.setStatus("Booked"); // Update to "Booked" if check-in is today
			} else if (checkInDate.isAfter(today)) {
				bookingRequest.setStatus("Prebooked"); // Keep "Prebooked" if it's a future booking
			}
			
			bookingRepository.save(bookingRequest);
		} else {
			throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
		}
		return bookingRequest.getBookingConfirmationCode();
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(
				() -> new ResourceNotFoundException("No booking found with booking code :" + confirmationCode));

	}

	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		return existingBookings.stream()
				.noneMatch(existingBooking -> bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
						|| bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
						|| (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
								&& bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
						|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

								&& bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
						|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

								&& bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

						|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
								&& bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

						|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
								&& bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())));
	}

}
