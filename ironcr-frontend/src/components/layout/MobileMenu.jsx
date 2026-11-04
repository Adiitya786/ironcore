import { Link } from "react-router-dom";
import { X } from "lucide-react";

const links = [
  { label: "Home", path: "/" },
  { label: "About", path: "/about" },
  { label: "Programs", path: "/programs" },
  { label: "Equipment", path: "/equipment" },
  { label: "Trainers", path: "/trainers" },
  { label: "Membership", path: "/membership" },
  { label: "Gallery", path: "/gallery" },
  { label: "FAQ", path: "/faq" },
  { label: "Contact", path: "/contact" },
];

export default function MobileMenu({ open, onClose }) {
  if (!open) return null;

  return (
    <div className="mobile-menu" role="dialog" aria-modal="true" aria-label="Mobile navigation">
      <div className="mobile-menu-overlay" onClick={onClose} aria-hidden="true" />
      <div className="mobile-menu-panel">
        <div className="mobile-menu-header">
          <Link to="/" className="navbar-logo" onClick={onClose} aria-label="IronCore Fitness home">
            <span className="logo-mark">IRONCORE</span>
            <span className="logo-sub">FITNESS</span>
          </Link>
          <button onClick={onClose} className="close-btn" aria-label="Close menu" aria-expanded={open}>
            <X size={22} strokeWidth={2} />
          </button>
        </div>
        <nav className="mobile-nav" aria-label="Mobile navigation">
          <ul>
            {links.map((l) => (
              <li key={l.path}>
                <Link to={l.path} onClick={onClose} className="mobile-nav-link">{l.label}</Link>
              </li>
            ))}
          </ul>
          <div className="mobile-cta">
            <Link to="/book-trial" className="btn-primary" onClick={onClose}>BOOK FREE TRIAL</Link>
          </div>
        </nav>
      </div>
    </div>
  );
}
