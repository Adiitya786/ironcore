import { Link } from "react-router-dom";

export default function FinalCTA() {
  return (
    <section className="cta-banner cta-dark" aria-label="Final call to action">
      <div className="cta-inner">
        <h2>READY TO START?</h2>
        <p>Your first step starts here.</p>
        <div className="cta-btn-row">
          <Link to="/book-trial" className="btn-primary btn-lg">BOOK FREE TRIAL</Link>
          <Link to="/contact" className="btn-outline btn-lg">CONTACT US</Link>
        </div>
      </div>
    </section>
  );
}
