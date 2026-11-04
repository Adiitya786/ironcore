import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { membershipApi } from "../../services/api";

export default function MembershipPreview() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    // Membership plans in DB are stored differently; here just show static from backend if available
    // For this preview we keep simple static since membership-plan endpoint isn't defined separately
    setItems([
      { id:1, name:"Basic", price:"₹999", period:"month" },
      { id:2, name:"Standard", price:"₹2499", period:"3 Months" },
      { id:3, name:"Premium", price:"₹7999", period:"year" },
    ]);
  }, []);
  return (
    <section className="section" aria-label="Membership preview">
      <div className="section-inner">
        <h2 className="section-heading">Membership Plans</h2>
        <div className="cards-grid cards-3">
          {items.map((m) => (
            <article key={m.id} className="membership-card">
              <h3>{m.name}</h3>
              <p className="price">{m.price}<span>/ {m.period}</span></p>
              <Link to="/join" className="btn-primary btn-sm">JOIN NOW</Link>
            </article>
          ))}
        </div>
        <div className="section-cta">
          <Link to="/join" className="btn-outline btn-md">VIEW ALL PLANS</Link>
        </div>
      </div>
    </section>
  );
}
