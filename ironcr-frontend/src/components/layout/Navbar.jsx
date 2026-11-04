import { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import { Menu } from "lucide-react";
import MobileMenu from "./MobileMenu";

import { isAdmin } from "../../services/api";

const navLinks = [
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

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 30);
    window.addEventListener("scroll", onScroll, { passive: true });
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  useEffect(() => {
    setMobileOpen(false);
    window.scrollTo(0, 0);
  }, [location.pathname]);

  return (
    <>
      <header
        className={`navbar ${scrolled ? "scrolled" : ""}`}
        aria-label="Primary navigation"
      >
        <div className="navbar-inner">
          <Link to="/" className="navbar-logo" aria-label="IronCore Fitness home">
            <span className="logo-mark">IRONCORE</span>
            <span className="logo-sub">FITNESS</span>
          </Link>

          <nav className="navbar-desktop" aria-label="Desktop navigation">
            <ul className="nav-list">
              {navLinks.map((l) => (
                <li key={l.path}>
                  <Link
                    to={l.path}
                    className={`nav-link ${location.pathname === l.path ? "active" : ""}`}
                  >
                    {l.label}
                  </Link>
                </li>
              ))}
            </ul>
          </nav>

          <div className="navbar-right">
            <Link to="/book-trial" className="btn-primary btn-sm">
              BOOK FREE TRIAL
            </Link>
            {isAdmin() && (
              <Link to="/admin/dashboard" className="btn-outline btn-sm" style={{ borderColor: "#c89f5e", color: "#c89f5e" }}>
                ADMIN
              </Link>
            )}
            <button
              className="hamburger"
              onClick={() => setMobileOpen(true)}
              aria-label="Open menu"
              aria-expanded={mobileOpen}
            >
              <Menu size={22} strokeWidth={2} />
            </button>
          </div>
        </div>
      </header>

      <MobileMenu open={mobileOpen} onClose={() => setMobileOpen(false)} />
    </>
  );
}
