import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authApi } from "../services/api";

/**
 * Register Page - PUBLIC
 * Allows anyone to create a user account.
 * After registration, redirects based on role (default: ROLE_USER).
 */
export default function RegisterPage() {
  const [formData, setFormData] = useState({ username: "", email: "", password: "", confirmPassword: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Validation
    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }
    if (formData.password.length < 6) {
      setError("Password must be at least 6 characters.");
      return;
    }

    setLoading(true);

    try {
      const result = await authApi.register(formData.username, formData.email, formData.password);
      navigate(result.role === "ROLE_ADMIN" ? "/admin/dashboard" : "/");
    } catch (err) {
      setError(err.message || "Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Register</h1>
          <p>Create your account to access member features.</p>
        </div>
      </section>

      <section className="section">
        <div className="section-inner" style={{ maxWidth: "500px", margin: "0 auto" }}>
          <form onSubmit={handleSubmit} aria-label="Registration form">
            <h2>Register</h2>

            <label htmlFor="regUsername">Username</label>
            <input
              id="regUsername"
              type="text"
              value={formData.username}
              onChange={e => setFormData({ ...formData, username: e.target.value })}
              placeholder="user@example.com"
              required
            />

            <label htmlFor="regEmail">Email</label>
            <input
              id="regEmail"
              type="email"
              value={formData.email}
              onChange={e => setFormData({ ...formData, email: e.target.value })}
              placeholder="user@example.com"
              required
            />

            <label htmlFor="regPassword">Password</label>
            <input
              id="regPassword"
              type="password"
              value={formData.password}
              onChange={e => setFormData({ ...formData, password: e.target.value })}
              placeholder="At least 6 characters"
              required
            />

            <label htmlFor="regConfirm">Confirm Password</label>
            <input
              id="regConfirm"
              type="password"
              value={formData.confirmPassword}
              onChange={e => setFormData({ ...formData, confirmPassword: e.target.value })}
              placeholder="Repeat your password"
              required
            />

            {error && <div className="error-message" style={{ marginBottom: "1rem" }}>{error}</div>}

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? "REGISTERING..." : "REGISTER"}
            </button>
          </form>
          <p style={{ textAlign: "center", marginTop: "1rem" }}>
            Already have an account? <Link to="/login">Login here</Link>
          </p>
        </div>
      </section>
    </>
  );
}
