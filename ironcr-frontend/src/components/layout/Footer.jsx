import { Link } from "react-router-dom";
import { Globe, Share2 } from "lucide-react";

export default function Footer() {
  return (
    <footer className="site-footer" aria-label="Site footer">
      <div className="footer-inner">
        <div className="footer-brand">
          <Link to="/" className="footer-logo" aria-label="IronCore Fitness home">
            <span className="logo-mark">IRONCORE</span>
            <span className="logo-sub">FITNESS</span>
          </Link>
          <p className="footer-desc">
            A premium modern gym built for real results. Expert trainers, modern equipment, and a clean professional environment.
          </p>
        </div>

        <div className="footer-columns">
          <div className="footer-col">
            <h4>Quick Links</h4>
            <ul>
              {["Home", "About", "Programs", "Trainers", "Membership", "Gallery", "Contact"].map((label) => {
                const path = label === "Home" ? "/" : `/${label.toLowerCase()}`;
                return (
                  <li key={label}><Link to={path}>{label}</Link></li>
                );
              })}
            </ul>
          </div>

          <div className="footer-col">
            <h4>Programs</h4>
            <ul>
              {["Weight Loss", "Muscle Gain", "Strength Training", "Personal Training", "Functional Training"].map((label) => (
                <li key={label}><Link to="/programs">{label}</Link></li>
              ))}
            </ul>
          </div>

          <div className="footer-col">
            <h4>Contact</h4>
            <ul>
              <li>Sector 12, Near City Mall, Gurugram</li>
              <li>+91 98765 43210</li>
              <li>hello@ironcorefitness.com</li>
            </ul>
            <div className="social-row">
              <a href="#" aria-label="Instagram"><Globe size={18} /></a>
              <a href="#" aria-label="Facebook"><Share2 size={18} /></a>
              <a href="#" aria-label="YouTube"><Globe size={18} /></a>
            </div>
          </div>
        </div>
      </div>

      <div className="footer-bottom">
        <p>© 2026 IronCore Fitness</p>
        <div className="footer-legal">
          <a href="#">Privacy Policy</a>
          <a href="#">Terms & Conditions</a>
        </div>
      </div>
    </footer>
  );
}
