package com.fourinx.HotelBookingApplication.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fourinx.HotelBookingApplication.exception.InvalidBookingRequestException;
import com.fourinx.HotelBookingApplication.exception.ResourceNotFoundException;
import com.fourinx.HotelBookingApplication.model.BookedRoom;
import com.fourinx.HotelBookingApplication.model.Room;
import com.fourinx.HotelBookingApplication.request.DashboardRequest;
import com.fourinx.HotelBookingApplication.response.BookingResponse;
import com.fourinx.HotelBookingApplication.response.DashBoardResponse;
import com.fourinx.HotelBookingApplication.response.RoomResponse;
import com.fourinx.HotelBookingApplication.service.BillingService;
import com.fourinx.HotelBookingApplication.service.DashBoardService;
import com.fourinx.HotelBookingApplication.service.IBookingService;
import com.fourinx.HotelBookingApplication.service.IRoomService;
import com.fourinx.HotelBookingApplication.service.RevenueService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
	private final IBookingService bookingService;
	private final IRoomService roomService;
	@Autowired
	private BillingService billingService;

	@Autowired
	private DashBoardService dashboardService;
	@Autowired
	private final RevenueService revenueService;

	@GetMapping("/all-bookings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<BookingResponse>> getAllBookings() {
		List<BookedRoom> bookings = bookingService.getAllBookings();
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for (BookedRoom booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);
	}

	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
		try {
			BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
			BookingResponse bookingResponse = getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponse);
		} catch (ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@GetMapping("/user/{email}/bookings")
	public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
		List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for (BookedRoom booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}

		return ResponseEntity.ok(bookingResponses);
	}

	/*
	 * @DeleteMapping("/booking/{bookingId}/delete") public void
	 * cancelBooking(@PathVariable Long bookingId) {
	 * bookingService.cancelBooking(bookingId); }
	 */
	/*
	 * @DeleteMapping("/booking/{bookingId}/delete") public ResponseEntity<String>
	 * cancelBooking(@PathVariable Long bookingId) {
	 * bookingService.cancelBooking(bookingId); return
	 * ResponseEntity.ok("Booking cancelled successfully."); }
	 */

	@DeleteMapping("/booking/{bookingId}/delete")
	public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {

		boolean isCancelled = bookingService.cancelBooking(bookingId);

		if (isCancelled) {
			return ResponseEntity.ok("Booking cancelled successfully.");
		} else {
			return ResponseEntity.badRequest()
					.body("Booking cannot be cancelled. It is either not 'Prebooked' or past the cancellation time.");
		}
	}

	private BookingResponse getBookingResponse(BookedRoom booking) {
		Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
		RoomResponse room = new RoomResponse(theRoom.getId(), theRoom.getRoomType(), theRoom.getRoomPrice());
		return new BookingResponse(booking.getBookingId(), booking.getCheckInDate(), booking.getCheckOutDate(),
				booking.getGuestFullName(), booking.getGuestEmail(), booking.getNumOfAdults(),
				booking.getNumOfChildren(), booking.getTotalNumOfGuest(), booking.getBookingConfirmationCode(),
				booking.getPhone(), booking.getIdProof(), booking.getStatus(), room);
	}

	/*
	 * @GetMapping("/pdf/{bookingId}") public ResponseEntity<byte[]>
	 * generatePdfInvoice(@PathVariable Long bookingId) throws IOException { byte[]
	 * pdfBytes = billingService.generatePdfInvoice(bookingId);
	 * 
	 * return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
	 * "attachment; filename=invoice.pdf")
	 * .contentType(MediaType.APPLICATION_PDF).body(pdfBytes); }
	 */

	@GetMapping("/excel/{bookingId}")
	public ResponseEntity<byte[]> generateExcelInvoice(@PathVariable Long bookingId) throws IOException {
		byte[] excelBytes = billingService.generateExcelInvoice(bookingId);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.xlsx")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(excelBytes);
	}

	@GetMapping("/word/{bookingId}")
	public ResponseEntity<byte[]> generateWordInvoice(@PathVariable Long bookingId) throws IOException {
		byte[] wordBytes = billingService.generateWordInvoice(bookingId);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.docx")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(wordBytes);
	}

	@GetMapping("/pdf/{bookingId}")
	public void generateInvoice(@PathVariable Long bookingId, HttpServletResponse response) throws IOException {
		billingService.generateInvoice(response, bookingId);
	}

	@PostMapping("/room/{roomId}/booking")
	public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest) {
		try {
			String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
			return ResponseEntity
					.ok("Room booked successfully, Your booking confirmation code is :" + confirmationCode);

		} catch (InvalidBookingRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// @CrossOrigin("*") // Allow frontend requests
	@PostMapping("/dashboard")
	public ResponseEntity<DashBoardResponse> getDashboardStats(@RequestBody DashboardRequest request) {
		DashBoardResponse response = dashboardService.getDashboardStats(request);
		return ResponseEntity.ok(response); // Returns 200 OK with the response body
	}

	@GetMapping("/revenue")

	public List<Map<String, Object>> getRevenue(@RequestParam int months) {
		return revenueService.getRevenueData(months);
	}
}