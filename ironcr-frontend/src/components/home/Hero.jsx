import { Link } from "react-router-dom";
import { MapPin, Clock } from "lucide-react";

export default function Hero() {
  return (
    <section className="hero" aria-label="Hero">
      <div className="hero-bg" aria-hidden="true">
        <img src="https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=1600&q=80" alt="" loading="eager" />
      </div>
      <div className="hero-inner">
        <h1 className="hero-title">
          <span>YOUR FITNESS.</span>
          <span>YOUR STRENGTH.</span>
          <span>YOUR JOURNEY.</span>
        </h1>
        <p className="hero-sub">Train smarter, get stronger, and build a healthier lifestyle with expert guidance and modern facilities.</p>
        <div className="hero-cta-row">
          <Link to="/book-trial" className="btn-primary btn-lg">BOOK FREE TRIAL</Link>
          <Link to="/membership" className="btn-outline btn-lg">VIEW MEMBERSHIPS</Link>
        </div>
        <div className="hero-meta">
          <div className="hero-meta-item">
            <MapPin size={16} aria-hidden="true" />
            <span>Sector 12, Gurugram</span>
          </div>
          <div className="hero-meta-item">
            <Clock size={16} aria-hidden="true" />
            <span>5:00 AM - 11:00 PM</span>
          </div>
        </div>
      </div>
    </section>
  );
}
