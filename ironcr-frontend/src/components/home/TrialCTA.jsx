import { Link } from "react-router-dom";

export default function TrialCTA() {
  return (
    <section className="cta-banner" aria-label="Free trial call to action">
      <div className="cta-inner">
        <h2>NOT SURE IF WE'RE RIGHT FOR YOU?</h2>
        <p>Come experience IronCore Fitness before you commit.</p>
        <Link to="/book-trial" className="btn-primary btn-lg">BOOK FREE TRIAL</Link>
      </div>
    </section>
  );
}
