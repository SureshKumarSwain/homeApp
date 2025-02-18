package com.fourinx.HotelBookingApplication.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	@Column(name = "check_in")
	private LocalDate checkInDate;

	@Column(name = "check_out")
	private LocalDate checkOutDate;

	@Column(name = "guest_fullName")
	private String guestFullName;

	@Column(name = "guest_email")
	private String guestEmail;

	@Column(name = "adults")
	private int NumOfAdults;

	@Column(name = "children")
	private int NumOfChildren;

	@Column(name = "total_guest")
	private int totalNumOfGuest;

	@Column(name = "confirmation_Code")
	private String bookingConfirmationCode;

	@Column(name = "status")
	private String status;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "id_proof")
	private String idProof;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "created_date")
	private LocalDate createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	public void calculateTotalNumberOfGuest() {
		this.totalNumOfGuest = this.NumOfAdults + NumOfChildren;
	}

	public void setNumOfAdults(int numOfAdults) {
		NumOfAdults = numOfAdults;
		calculateTotalNumberOfGuest();
	}

	public void setNumOfChildren(int numOfChildren) {
		NumOfChildren = numOfChildren;
		calculateTotalNumberOfGuest();
	}

	public void setBookingConfirmationCode(String bookingConfirmationCode) {
		this.bookingConfirmationCode = bookingConfirmationCode;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setIdProof(String idProof) {
		this.idProof = idProof;
	}

}
