import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { programsApi, isAdmin } from "../services/api";

/**
 * Programs Page - PUBLIC
 * Fetches programs from backend and displays them.
 * Admin users see additional "Add Program" button.
 */
export default function Programs() {
  const [programs, setPrograms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchPrograms();
  }, []);

  const fetchPrograms = async () => {
    try {
      setLoading(true);
      const data = await programsApi.getAll();
      setPrograms(data);
      setError(null);
    } catch (err) {
      setError("Failed to load programs. Please try again later.");
      console.error("Error fetching programs:", err);
    } finally {
      setLoading(false);
    }
  };

  // Helper to decode base64 image data from backend
  const getImageSrc = (program) => {
    if (program.imageData && program.imageType) {
      return `data:${program.imageType};base64,${program.imageData}`;
    }
    // Fallback to static data image if no backend image
    return "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=800&q=80";
  };

  // Parse benefits from JSON string or array
  const getBenefits = (benefits) => {
    if (!benefits) return [];
    if (Array.isArray(benefits)) return benefits;
    try {
      return JSON.parse(benefits);
    } catch {
      return [benefits];
    }
  };

  if (loading) {
    return (
      <>
        <section className="page-hero">
          <div className="page-hero-inner">
            <h1>Our Programs</h1>
            <p>Loading programs...</p>
          </div>
        </section>
      </>
    );
  }

  if (error) {
    return (
      <>
        <section className="page-hero">
          <div className="page-hero-inner">
            <h1>Our Programs</h1>
            <p className="error-message">{error}</p>
            <button onClick={fetchPrograms} className="btn-primary">Retry</button>
          </div>
        </section>
      </>
    );
  }

  return (
    <>
      <section className="page-hero" aria-label="Programs hero">
        <div className="page-hero-inner">
          <h1>Our Programs</h1>
          <p>Programs built for your goals — weight loss, strength, mobility, and more.</p>
          {isAdmin() && (
            <Link to="/admin/programs/new" className="btn-primary" style={{ marginTop: "1rem" }}>
              + ADD NEW PROGRAM
            </Link>
          )}
        </div>
      </section>

      <section className="section" aria-label="All programs">
        <div className="section-inner">
          {programs.length === 0 ? (
            <div style={{ textAlign: "center", padding: "3rem" }}>
              <p>No programs available yet.</p>
              {isAdmin() && (
                <Link to="/admin/programs/new" className="btn-primary">Add Your First Program</Link>
              )}
            </div>
          ) : (
            <div className="cards-grid cards-3">
              {programs.map((program) => (
                <article className="program-card" key={program.id}>
                  <img
                    src={getImageSrc(program)}
                    alt={program.title}
                    loading="lazy"
                  />
                  <h2>{program.title}</h2>
                  <p>{program.description}</p>
                  {program.benefits && (
                    <ul className="benefits-list">
                      {getBenefits(program.benefits).map((b, i) => (
                        <li key={i}>{b}</li>
                      ))}
                    </ul>
                  )}
                  <Link to="/book-trial" className="btn-primary btn-sm">
                    ENQUIRE NOW
                  </Link>
                </article>
              ))}
            </div>
          )}
        </div>
      </section>
    </>
  );
}
