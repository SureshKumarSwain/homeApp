import React, { useState, useEffect } from "react";
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from "recharts";

const RevenueChart = () => {
    const [revenueData, setRevenueData] = useState([]);
    const [selectedRange, setSelectedRange] = useState(3); // Default: Last 3 months
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchRevenueData(selectedRange);
    }, [selectedRange]);

    const fetchRevenueData = async (months) => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch(`http://localhost:8080/bookings/revenue?months=${months}`);
            if (!response.ok) throw new Error("Failed to fetch revenue data");

            const data = await response.json();
            setRevenueData(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white p-4 rounded-lg shadow-md">
            {/* Header Section */}
            <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold">Revenue</h3>
                <select
                    value={selectedRange}
                    onChange={(e) => setSelectedRange(parseInt(e.target.value))}
                    className="p-2 border rounded-md text-gray-700"
                >
                    <option value={3}>Last 3 Months</option>
                    <option value={6}>Last 6 Months</option>
                    <option value={12}>Last Year</option>
                </select>
            </div>

            {/* Show Loading / Error */}
            {loading && <p>Loading revenue data...</p>}
            {error && <p className="text-red-500">Error: {error}</p>}

            {/* Chart Container */}
            {revenueData.length > 0 && (
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={revenueData}>
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip formatter={(value) => `$${value.toLocaleString()}`} />
                        <CartesianGrid strokeDasharray="3 3" />
                        <Line type="monotone" dataKey="totalRevenue" stroke="#A3C920" strokeWidth={2} dot={{ r: 5 }} />
                    </LineChart>
                </ResponsiveContainer>
            )}
        </div>
    );
};

export default RevenueChart;
