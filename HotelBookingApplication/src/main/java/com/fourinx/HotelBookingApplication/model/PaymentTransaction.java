package com.fourinx.HotelBookingApplication.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "payment_transaction")
public class PaymentTransaction {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;
    private String name;
    private String email;
    private String checkInDate;
    private String checkOutDate;
    private int adults;
    private int children;
    private double amount;
    private String status; // PENDING, SUCCESS, FAILED

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String paymentResponseJson;

    private LocalDateTime createdAt;

    public PaymentTransaction() {
        this.createdAt = LocalDateTime.now();
    }

    public PaymentTransaction(String bookingId, String name, String email, String checkInDate, String checkOutDate, int adults, int children, double amount, String status, String paymentResponseJson) {
        this.bookingId = bookingId;
        this.name = name;
        this.email = email;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.adults = adults;
        this.children = children;
        this.amount = amount;
        this.status = status;
        this.paymentResponseJson = paymentResponseJson;
        this.createdAt = LocalDateTime.now();
    }

   

}
