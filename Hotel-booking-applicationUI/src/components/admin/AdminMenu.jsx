import React from "react"
import { Link } from "react-router-dom"
import Sidebar from "./Sidebar";
import { Outlet } from "react-router-dom";

const AdminMenu = () => {

	return (
		<>
			<li>
				<Link className="dropdown-item" to={"/existing-rooms"}>
					Manage Rooms
				</Link>
			</li>
			<li>
				<hr className="dropdown-divider" />
			</li>
			<Link className="dropdown-item" to={"/existing-bookings"}>
				Manage Bookings
			</Link>
			<li>
							<hr className="dropdown-divider" />
						</li>
			<li>
				<Link className="dropdown-item" to={"/dashboard"}>
					Dashboard
				</Link>
			</li>
		</>
	)
}

export default AdminMenu
