import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { trainersApi } from "../../services/api";

export default function TrainersPreview() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    trainersApi.getAll().then(data => setItems(data || [])).catch(() => setItems([]));
  }, []);

  const getImage = (t) => {
    if (t.imageData && t.imageType) return `data:${t.imageType};base64,${t.imageData}`;
    return "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=600&q=80";
  };

  return (
    <section className="section section-alt" aria-label="Trainers preview">
      <div className="section-inner">
        <h2 className="section-heading">Our Trainers</h2>
        <div className="cards-grid cards-3">
          {items.slice(0, 3).map((t) => (
            <article key={t.id || t.name} className="trainer-card">
              <img src={getImage(t)} alt={t.name} loading="lazy" />
              <h3>{t.name}</h3>
              <p>{t.bio?.substring(0, 100) || t.specialization}</p>
            </article>
          ))}
        </div>
        {items.length === 0 && <p style={{textAlign:"center"}}>No trainers saved by admin yet.</p>}
        <div className="section-cta">
          <Link to="/trainers" className="btn-outline btn-md">VIEW ALL TRAINERS</Link>
        </div>
      </div>
    </section>
  );
}
