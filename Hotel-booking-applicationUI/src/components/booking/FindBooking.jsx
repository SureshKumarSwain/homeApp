import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  TableContainer,
  Paper,
  Select,
  MenuItem,
  CircularProgress,
  Typography,
} from "@mui/material";

const FindBooking = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filter, setFilter] = useState(""); // Default: Show all

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      const email = localStorage.getItem("userId");
      console.log("Fetching bookings...");
      const response = await axios.get(
        `http://localhost:8080/bookings/user/${email}/bookings`
      );

      console.log("API Response:", response.data); // Debugging
      setBookings(response.data);
      setLoading(false);
    } catch (err) {
      console.error("API Error:", err);
      setError("Failed to fetch bookings.");
      setLoading(false);
    }
  };

  // Filter bookings based on status
  const filteredBookings = filter
    ? bookings.filter((booking) => booking.status === filter)
    : bookings;

  return (
    <div style={{ padding: "20px" }}>
      <Typography variant="h4" gutterBottom>
        Bookings
      </Typography>

      {/* Status Filter Dropdown */}
      <Select
        value={filter}
        onChange={(e) => setFilter(e.target.value)}
        displayEmpty
        style={{ marginBottom: "20px", width: "200px" }}
      >
        <MenuItem value="">All</MenuItem>
        <MenuItem value="Booked">Booked</MenuItem>
        <MenuItem value="Cancelled">Cancelled</MenuItem>
        <MenuItem value="Prebooked">Prebooked</MenuItem> {/* Added Prebooked */}
      </Select>

      {loading ? (
        <CircularProgress />
      ) : error ? (
        <Typography color="error">{error}</Typography>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow style={{ backgroundColor: "#f5f5f5" }}>
                <TableCell><strong>ID</strong></TableCell>
                <TableCell><strong>Guest Name</strong></TableCell>
                <TableCell><strong>Guest Email</strong></TableCell>
                <TableCell><strong>Phone Number</strong></TableCell>
                <TableCell><strong>ID Proof</strong></TableCell>
                <TableCell><strong>Check-In Date</strong></TableCell>
                <TableCell><strong>Check-Out Date</strong></TableCell>
                <TableCell><strong>Total Guests</strong></TableCell>
                <TableCell><strong>Status</strong></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredBookings.length > 0 ? (
                filteredBookings.map((booking) => (
                  <TableRow key={booking.id}>
                    <TableCell>{booking.id}</TableCell>
                    <TableCell>{booking.guestName}</TableCell>
                    <TableCell>{booking.guestEmail}</TableCell>
                    <TableCell>{booking.phone|| "N/A"}</TableCell>
                    <TableCell>{booking.idProof || "N/A"}</TableCell>
                    <TableCell>{booking.checkInDate.join("-")}</TableCell>
                    <TableCell>{booking.checkOutDate.join("-")}</TableCell>
                    <TableCell>{booking.totalNumOfGuests}</TableCell>
                    <TableCell
                      style={{
                        color:
                          booking.status === "Cancelled"
                            ? "red"
                            : booking.status === "Booked"
                            ? "green"
                            : booking.status === "Prebooked"
                            ? "orange"
                            : "blue",
                        fontWeight: "bold",
                      }}
                    >
                      {booking.status}
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={9} align="center">
                    No bookings found.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </div>
  );
};

export default FindBooking;
