import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { trainersApi, isAdmin } from "../services/api";

/**
 * Trainers Page - PUBLIC
 * Displays all gym trainers with their specializations.
 * Admin can add/edit/delete trainers.
 */
export default function Trainers() {
  const [trainers, setTrainers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTrainers();
  }, []);

  const fetchTrainers = async () => {
    try {
      setLoading(true);
      const data = await trainersApi.getAll();
      setTrainers(data);
    } catch (err) {
      console.error("Error fetching trainers:", err);
    } finally {
      setLoading(false);
    }
  };

  const getImageSrc = (trainer) => {
    if (trainer.imageData && trainer.imageType) {
      return `data:${trainer.imageType};base64,${trainer.imageData}`;
    }
    return "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=600&q=80";
  };

  if (loading) {
    return (
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Our Trainers</h1>
          <p>Loading trainers...</p>
        </div>
      </section>
    );
  }

  return (
    <>
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Our Trainers</h1>
          <p>Certified professionals with years of real-world experience.</p>
        </div>
      </section>

      <section className="section">
        <div className="section-inner">
          <div className="cards-grid cards-3">
            {trainers.map(trainer => (
              <article key={trainer.id} className="trainer-card">
                <img src={getImageSrc(trainer)} alt={trainer.name} loading="lazy" />
                <h3>{trainer.name}</h3>
                {trainer.bio && <p>{trainer.bio}</p>}
                <Link to="/book-appointment" className="btn-outline btn-sm">
                  BOOK SESSION
                </Link>
              </article>
            ))}
          </div>

          {trainers.length === 0 && (
            <p style={{ textAlign: "center", padding: "2rem" }}>
              No trainers available yet.
            </p>
          )}
        </div>
      </section>
    </>
  );
}
