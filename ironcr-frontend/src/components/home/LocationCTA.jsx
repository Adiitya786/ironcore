import { Link } from "react-router-dom";
import { MapPin, Phone, Mail, Clock } from "lucide-react";

export default function LocationCTA() {
  return (
    <section className="section" aria-label="Location and contact">
      <div className="section-inner">
        <h2 className="section-heading">Visit Us</h2>
        <div className="location-two-col">
          <a href="https://maps.google.com/?q=Sector+12+Gurugram" target="_blank" rel="noopener noreferrer" className="map-frame" aria-label="Open location in Google Maps">
            <img src="https://images.unsplash.com/photo-1524661135-423995f22d0b?w=800&q=80" alt="Map location" loading="lazy" />
            <span className="map-label">GET DIRECTIONS</span>
          </a>
          <div className="contact-card">
            <h3>IRONCORE FITNESS</h3>
            <ul>
              <li><MapPin size={16} aria-hidden="true" /> <span>Sector 12, Near City Mall, Gurugram, Haryana 122001</span></li>
              <li><Phone size={16} aria-hidden="true" /> <span>+91 98765 43210</span></li>
              <li><Mail size={16} aria-hidden="true" /> <span>hello@ironcorefitness.com</span></li>
            </ul>
            <div className="hours-row">
              <Clock size={16} aria-hidden="true" />
              <div>
                <p>Mon - Fri: 5:00 AM - 11:00 PM</p>
                <p>Sat - Sun: 6:00 AM - 10:00 PM</p>
              </div>
            </div>
            <div className="contact-actions">
              <Link to="/contact" className="btn-primary btn-sm">CONTACT US</Link>
              <a href="tel:+919876543210" className="btn-outline btn-sm">CALL US</a>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
