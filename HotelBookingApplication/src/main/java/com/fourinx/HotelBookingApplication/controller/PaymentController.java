package com.fourinx.HotelBookingApplication.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fourinx.HotelBookingApplication.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	/*
	 * @Autowired private PaymentService paymentService;
	 */
	/*
	 * @PostMapping("/process") public ResponseEntity<PaymentResponse>
	 * processPayment(@RequestBody PaymentRequest request) { PaymentResponse
	 * response = paymentService.processHotelPayment(request); return
	 * ResponseEntity.ok(response); }
	 */
	
	/*
	 * @PostMapping("/process/{bookingId}") public ResponseEntity<Map<String,
	 * Object>> processPayment(@PathVariable String bookingId) { Map<String, Object>
	 * response = paymentService.initiatePayment(bookingId); return
	 * ResponseEntity.ok(response); }
	 */
    
	@Autowired
    private PaymentService upiPaymentService;

    @GetMapping("/upi-id")
    public String getUpiId() {
        return upiPaymentService.getUpiId();
    }


    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload-screenshot")
    public String uploadScreenshot(@RequestParam("bookingId") String bookingId,
                                   @RequestParam("file") MultipartFile file) {
        try {
            // Ensure the directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // Create directory if it doesn't exist
            }

            // Save file
            String filePath = UPLOAD_DIR + file.getOriginalFilename();
            File destFile = new File(filePath);
            file.transferTo(destFile);

            return "Payment Screenshot Uploaded Successfully! Path: " + filePath;
        } catch (IOException e) {
            return "Error uploading screenshot: " + e.getMessage();
        }
    }

    @PostMapping("/verify-payment")
    public String verifyPayment(@RequestParam("bookingId") String bookingId,
                                @RequestParam("isVerified") boolean isVerified) {
        return upiPaymentService.verifyPayment(bookingId, isVerified);
    }
}

