import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from "recharts";
import RevenueChart from "./RevenueChart";

const Dashboard = () => {
	const [dashboardData, setDashboardData] = useState(null);
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);
	const [startDate, setStartDate] = useState(new Date()); // Default to today
	const [endDate, setEndDate] = useState(new Date()); // Default to today

	const fetchDashboardData = async () => {
		setLoading(true);
		setError(null);

		try {
			const response = await fetch("http://localhost:8080/bookings/dashboard", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					startDate: startDate.toISOString().split("T")[0], // Send as YYYY-MM-DD
					endDate: endDate.toISOString().split("T")[0]
				})
			});

			if (!response.ok) {
				throw new Error("Failed to fetch dashboard data");
			}

			const data = await response.json();
			setDashboardData(data);
		} catch (err) {
			setError(err.message);
		} finally {
			setLoading(false);
		}
	};

	// Prepare data for Bar Chart
	const chartData = dashboardData
		? [
				{ name: "Booked Rooms", value: dashboardData.bookedRooms },
				{ name: "Available Rooms", value: dashboardData.availableRooms },
				{ name: "New Bookings", value: dashboardData.newBookings },
				{ name: "Check In", value: dashboardData.checkIn },
				{ name: "Check Out", value: dashboardData.checkOut }
		  ]
		: [];

	return (
		<div className="container">
			<h2 className="text-2xl font-bold text-gray-700">Dashboard</h2>

			{/* Calendar Filters */}
			<div className="date-filters">
				<label>Select Start Date: </label>
				<DatePicker selected={startDate} onChange={(date) => setStartDate(date)} />

				<label>Select End Date: </label>
				<DatePicker selected={endDate} onChange={(date) => setEndDate(date)} />

				<button onClick={fetchDashboardData} className="btn btn-primary">
					Fetch Data
				</button>
			</div>

			{/* Revenue Chart */}
			<div className="p-6 w-full">
				<h2 className="text-2xl font-bold text-gray-700">Revenue Chart</h2>
				<div className="mt-6">
					<RevenueChart />
				</div>
			</div>

			{/* Display Data */}
			{loading && <p>Loading dashboard data...</p>}
			{error && <p className="error">Error: {error}</p>}
			{dashboardData && (
				<div className="dashboard-details">
					<p><strong>Total Bookings:</strong> {dashboardData.bookedRooms}</p>
					<p><strong>Revenue:</strong> ${dashboardData.totalRevenue}</p>
					<p><strong>Available Rooms:</strong> {dashboardData.availableRooms}</p>
					<p><strong>New Bookings:</strong> {dashboardData.newBookings}</p>
					<p><strong>Check In:</strong> {dashboardData.checkIn}</p>
					<p><strong>Check Out:</strong> {dashboardData.checkOut}</p>
					<p><strong>Today's Date:</strong> {dashboardData.date}</p>
					<p><strong>Unoccupied Rooms:</strong> {dashboardData.unOccupiedRooms}</p>
				</div>
			)}

			{/* Bar Chart Visualization */}
			{dashboardData && (
				<div className="chart-container">
					<h3>Booking Data Overview</h3>
					<ResponsiveContainer width="100%" height={300}>
						<BarChart data={chartData}>
							<XAxis dataKey="name" />
							<YAxis />
							<Tooltip />
							<Legend />
							<Bar dataKey="value" fill="#82ca9d" />
						</BarChart>
					</ResponsiveContainer>
				</div>
			)}
		</div>
	);
};

export default Dashboard;
