package com.fourinx.HotelBookingApplication.response;

import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private double roomPrice;
    private boolean isBooked;
    private String photo;
    private List<BookingResponse>bookings;

    public RoomResponse(Long id, String roomType, double d) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = d;
    }

    public RoomResponse(Long id, String roomType, double roomPrice, boolean isBooked,
                        byte[] photoBytes , List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
       this.bookings = bookings;
    }

}
