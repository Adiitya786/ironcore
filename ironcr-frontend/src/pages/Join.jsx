import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { membershipApi, authApi } from "../services/api";

/**
 * Join Page - PUBLIC
 * Form to submit membership request to backend.
 * Data is saved to database via POST /api/membership-requests.
 */
export default function Join() {
  const [formData, setFormData] = useState({
    fullName: "",
    phone: "",
    email: "",
    plan: "basic",
    preferredStartDate: "",
    fitnessGoal: ""
  });
  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Plan options
  const plans = [
    { id: "basic", name: "Basic", price: "₹999", period: "Monthly" },
    { id: "standard", name: "Standard", price: "₹2499", period: "3 Months" },
    { id: "premium", name: "Premium", price: "₹7999", period: "Yearly" }
  ];

  // Handle plan selection
  const handlePlanChange = (e) => {
    setFormData({ ...formData, plan: e.target.value });
  };

  // Validate and submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});

    // ===== STEP 1: Validate fields =====
    const newErrors = {};

    if (!formData.fullName.trim()) newErrors.fullName = "Full name is required";
    if (!formData.phone.trim()) newErrors.phone = "Phone number is required";
    else if (!/^\+?[0-9\s-]{7,}$/.test(formData.phone)) newErrors.phone = "Invalid phone format";
    if (formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) newErrors.email = "Invalid email";
    if (!formData.fitnessGoal.trim()) newErrors.fitnessGoal = "Fitness goal is required";

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) return;

    // ===== STEP 2: Submit to backend =====
    setLoading(true);
    try {
      await membershipApi.create({
        fullName: formData.fullName,
        phone: formData.phone,
        email: formData.email,
        plan: formData.plan,
        preferredStartDate: formData.preferredStartDate,
        fitnessGoal: formData.fitnessGoal
      });

      // ===== STEP 3: Show success =====
      setSuccess(true);
      setTimeout(() => {
        navigate("/"); // Redirect to home on success
      }, 2000);

    } catch (err) {
      console.error("Error submitting membership request:", err);
      setErrors({ submitError: err.message || "Something went wrong. Please try again." });
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <>
        <section className="page-hero">
          <div className="page-hero-inner">
            <h1>Membership Request Submitted!</h1>
            <p>Thank you for your interest. We will contact you shortly.</p>
          </div>
        </section>
      </>
    );
  }

  return (
    <>
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Join IronCore Fitness</h1>
          <p>Choose your plan and complete the form below.</p>
        </div>
      </section>

      <section className="section">
        <div className="section-inner two-col">
          <form onSubmit={handleSubmit} noValidate aria-label="Membership join form">
            {/* Plan Selection */}
            <h2 style={{ marginTop: "1rem" }}>Select Your Plan</h2>
            <div className="plan-selector" style={{ display: "flex", gap: "1rem", flexWrap: "wrap" }}>
              {plans.map(plan => (
                <label key={plan.id} style={{ cursor: "pointer" }}>
                  <input
                    type="radio"
                    name="plan"
                    value={plan.id}
                    checked={formData.plan === plan.id}
                    onChange={handlePlanChange}
                  />
                  <span style={{ fontWeight: "bold" }}>{plan.name}</span>
                  <span style={{ color: "#666" }}>{plan.price} / {plan.period}</span>
                </label>
              ))}
            </div>

            {/* Name */}
            <label htmlFor="fullName" style={{ display: "block", marginTop: "1rem" }}>
              Full Name *
            </label>
            <input id="fullName" value={formData.fullName} onChange={e => setFormData({...formData, fullName: e.target.value})} placeholder="John Doe" />
            {errors.fullName && <span className="error-message">{errors.fullName}</span>}

            {/* Phone */}
            <label htmlFor="phone" style={{ display: "block", marginTop: "0.5rem" }}>
              Phone Number *
            </label>
            <input id="phone" value={formData.phone} onChange={e => setFormData({...formData, phone: e.target.value})} placeholder="+91 98765 43210" />
            {errors.phone && <span className="error-message">{errors.phone}</span>}

            {/* Email */}
            <label htmlFor="email" style={{ display: "block", marginTop: "0.5rem" }}>
              Email
            </label>
            <input id="email" type="email" value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} placeholder="john@example.com" />
            {errors.email && <span className="error-message">{errors.email}</span>}

            {/* Preferred Start Date */}
            <label htmlFor="startDate" style={{ display: "block", marginTop: "0.5rem" }}>
              Preferred Start Date
            </label>
            <input id="startDate" type="date" value={formData.preferredStartDate} onChange={e => setFormData({...formData, preferredStartDate: e.target.value})} />

            {/* Fitness Goal */}
            <label htmlFor="goal" style={{ display: "block", marginTop: "0.5rem" }}>
              Fitness Goal *
            </label>
            <textarea id="goal" rows={3} value={formData.fitnessGoal} onChange={e => setFormData({...formData, fitnessGoal: e.target.value})} placeholder="Weight loss, muscle gain, strength training, etc." />
            {errors.fitnessGoal && <span className="error-message">{errors.fitnessGoal}</span>}

            {/* Submit Button */}
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? "SUBMITTING..." : "SUBMIT REQUEST"}
            </button>

            {errors.submitError && <span className="error-message">{errors.submitError}</span>}
          </form>
        </div>
      </section>
    </>
  );
}
