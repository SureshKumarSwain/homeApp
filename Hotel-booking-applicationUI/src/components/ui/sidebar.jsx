import { Link } from "react-router-dom";

const Sidebar = () => {
    return (
        <div className="w-64 h-screen bg-gray-800 text-white p-4">
            <h2 className="text-2xl font-bold mb-6">Hotel Admin</h2>
            <ul className="space-y-4">
                <li><Link to="/dashboard" className="block px-4 py-2 hover:bg-gray-700 rounded">Dashboard</Link></li>
                <li><Link to="/bookings" className="block px-4 py-2 hover:bg-gray-700 rounded">Bookings</Link></li>
                <li><Link to="/rooms" className="block px-4 py-2 hover:bg-gray-700 rounded">Rooms</Link></li>
                <li><Link to="/payments" className="block px-4 py-2 hover:bg-gray-700 rounded">Payments</Link></li>
                <li><Link to="/settings" className="block px-4 py-2 hover:bg-gray-700 rounded">Settings</Link></li>
            </ul>
        </div>
    );
};

export { Sidebar };
