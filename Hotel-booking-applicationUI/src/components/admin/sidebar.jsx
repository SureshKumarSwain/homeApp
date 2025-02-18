import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FaBars,
  FaUser,
  FaChartPie,
  FaUsers,
  FaClipboardList,
  FaTasks,
  FaBug,
} from "react-icons/fa";

const Sidebar = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const navigate = useNavigate();

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const handleNavigation = (path) => {
    navigate(path);
  };

  return (
    <aside
      className={`bg-black text-white p-5 flex flex-col fixed left-0 top-0 h-screen transition-all duration-300 ${
        isSidebarOpen ? "w-64" : "w-16"
      }`}
    >
      <div className="flex items-center mb-6">
        {isSidebarOpen && (
          <h2 className="text-lg font-bold flex items-center">
            Welcome Admin
            <FaBars className="cursor-pointer text-xl ml-2" onClick={toggleSidebar} />
          </h2>
        )}
        {!isSidebarOpen && <FaBars className="cursor-pointer text-xl" onClick={toggleSidebar} />}
      </div>

      <nav className="flex-1">
        <ul className="list-none p-0 m-0">
          <li className="p-3 bg-gray-800 rounded my-2 flex items-center cursor-pointer"
              onClick={() => handleNavigation("/admin/dashboard")}>
            <FaChartPie className="text-lg" />
            {isSidebarOpen && <span className="ml-2">Dashboard</span>}
          </li>
          <li className="p-3 hover:bg-gray-800 rounded my-2 flex items-center cursor-pointer"
              onClick={() => handleNavigation("/admin/customers")}>
            <FaUsers className="text-lg" />
            {isSidebarOpen && <span className="ml-2">Customers</span>}
          </li>
          <li className="p-3 hover:bg-gray-800 rounded my-2 flex items-center cursor-pointer"
              onClick={() => handleNavigation("/admin/bookings")}>
            <FaClipboardList className="text-lg" />
            {isSidebarOpen && <span className="ml-2">Bookings</span>}
          </li>
          <li className="p-3 hover:bg-gray-800 rounded my-2 flex items-center cursor-pointer"
              onClick={() => handleNavigation("/admin/new-booking")}>
            <FaTasks className="text-lg" />
            {isSidebarOpen && <span className="ml-2">New Booking</span>}
          </li>
          <li className="p-3 hover:bg-gray-800 rounded my-2 flex items-center cursor-pointer"
              onClick={() => handleNavigation("/admin/management")}>
            <FaBug className="text-lg" />
            {isSidebarOpen && <span className="ml-2">Management</span>}
          </li>
          <li className="p-3 hover:bg-gray-800 rounded my-2 flex items-center cursor-pointer"
              onClick={() => handleNavigation("/admin/users")}>
            <FaUser className="text-lg" />
            {isSidebarOpen && <span className="ml-2">Users</span>}
          </li>
          <li className="p-3 hover:bg-gray-800 rounded my-2 flex items-center cursor-pointer"
              onClick={() => handleNavigation("/admin/reports")}>
            <FaUser className="text-lg" />
            {isSidebarOpen && <span className="ml-2">Reports</span>}
          </li>
        </ul>
      </nav>
    </aside>
  );
};

export default Sidebar;
