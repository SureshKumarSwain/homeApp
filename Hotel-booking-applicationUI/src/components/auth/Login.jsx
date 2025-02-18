import React, { useState } from "react"
import { loginUser } from "../utils/ApiFunctions"
import { Link, useLocation, useNavigate } from "react-router-dom"
import { useAuth } from "./AuthProvider"

const Login = () => {
	const [errorMessage, setErrorMessage] = useState("")
	const [login, setLogin] = useState({
		email: "",
		password: ""
	})

	const navigate = useNavigate()
	const auth = useAuth()
	const location = useLocation()
	const redirectUrl = location.state?.path || "/"

	const handleInputChange = (e) => {
		setLogin({ ...login, [e.target.name]: e.target.value })
	}

	const handleSubmit = async (e) => {
		e.preventDefault()
		const success = await loginUser(login)
		if (success) {
			const token = success.token
			auth.handleLogin(token)
			navigate(redirectUrl, { replace: true })
		} else {
			setErrorMessage("Invalid username or password. Please try again.")
		}
		setTimeout(() => {
			window.location.reload()
			setErrorMessage("")
		}, 1000)
	}

	return (
		<section className="container d-flex justify-content-center align-items-center min-vh-100">
			<div className="row shadow-lg rounded overflow-hidden w-100" style={{ maxWidth: "900px" }}>
				
				{/* Left Side - Login Form */}
				<div className="col-md-6 p-5 bg-white">
					{errorMessage && <p className="alert alert-danger">{errorMessage}</p>}
					<h2 className="mb-4">Login</h2>
					<form onSubmit={handleSubmit}>
						<div className="mb-3">
							<label htmlFor="email" className="form-label">Email</label>
							<input
								id="email"
								name="email"
								type="email"
								className="form-control"
								value={login.email}
								onChange={handleInputChange}
							/>
						</div>

						<div className="mb-3">
							<label htmlFor="password" className="form-label">Password</label>
							<input
								id="password"
								name="password"
								type="password"
								className="form-control"
								value={login.password}
								onChange={handleInputChange}
							/>
						</div>

						<div className="mb-3">
							<button type="submit" className="btn btn-primary w-100">
								Login
							</button>
						</div>

						<p className="text-center">
							Don't have an account? <Link to={"/register"}>Register</Link>
						</p>
					</form>
				</div>

				{/* Right Side - Reception Image (Now Visible on Phones) */}
				<div className="col-md-6 d-flex justify-content-center align-items-center p-3">
					<img 
						src="/src/assets/images/reciption.jpg"
						alt="Reception" 
						className="img-fluid rounded"
						style={{ maxHeight: "400px", width: "100%", objectFit: "cover" }}
					/>
				</div>

			</div>
		</section>
	)
}

export default Login
