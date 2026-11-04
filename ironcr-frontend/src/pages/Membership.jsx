import { useState } from "react";
import { Link } from "react-router-dom";
import { membershipApi, isAdmin } from "../services/api";

/**
 * Membership Page - PUBLIC
 * Shows 3 membership plans and links to Join form.
 */
export default function Membership() {
  const [submitted, setSubmitted] = useState(false);

  const plans = [
    { id: "basic", name: "Basic", price: "₹999", period: "month", features: ["Gym Access", "Cardio Area", "Weight Training", "Shower Access"] },
    { id: "standard", name: "Standard", price: "₹2499", period: "3 Months", features: ["Gym Access", "Cardio Area", "Weight Training", "Fitness Assessment", "Locker Storage"] },
    { id: "premium", name: "Premium", price: "₹7999", period: "year", features: ["Full Gym Access", "Personal Training Sessions", "Fitness Assessment", "Diet Guidance", "Priority Booking"] }
  ];

  return (
    <>
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Membership Plans</h1>
          <p>Choose the plan that fits your goals and commitment.</p>
        </div>
      </section>

      <section className="section">
        <div className="section-inner">
          <div className="cards-grid cards-3">
            {plans.map(plan => (
              <article key={plan.id} className="plan-card">
                <h2>{plan.name}</h2>
                <div className="plan-price">{plan.price}<span>/{plan.period}</span></div>
                <ul className="plan-features">
                  {plan.features.map(f => <li key={f}>{f}</li>)}
                </ul>
                <Link to="/join" className="btn-primary btn-sm">JOIN NOW</Link>
              </article>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}
