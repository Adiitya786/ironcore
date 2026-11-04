import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authApi } from "../services/api";

/**
 * Login Page - PUBLIC
 * Allows users and admins to login.
 * After successful login, redirects based on role.
 */
export default function LoginPage() {
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const result = await authApi.login(formData.username, formData.password);

      // After successful login, redirect based on role
      if (result.role === "ROLE_ADMIN") {
        navigate("/admin/dashboard");
      } else {
        navigate("/");
      }
    } catch (err) {
      setError(err.message || "Login failed. Please check your credentials.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Login</h1>
          <p>Access your account.</p>
        </div>
      </section>

      <section className="section">
        <div className="section-inner" style={{ maxWidth: "500px", margin: "0 auto" }}>
          <form onSubmit={handleSubmit} aria-label="Login form">
            <h2>Login</h2>

            <label htmlFor="username">Username or Email</label>
            <input
              id="username"
              type="text"
              value={formData.username}
              onChange={e => setFormData({ ...formData, username: e.target.value })}
              placeholder="Enter username or email"
              required
            />

            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              value={formData.password}
              onChange={e => setFormData({ ...formData, password: e.target.value })}
              placeholder="Enter password"
              required
            />

            {error && <div className="error-message" style={{ marginBottom: "1rem" }}>{error}</div>}

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? "LOGGING IN..." : "LOGIN"}
            </button>
          </form>

          <p style={{ textAlign: "center", marginTop: "1rem" }}>
            Don't have an account? <Link to="/register">Register here</Link>
          </p>
        </div>
      </section>
    </>
  );
}
