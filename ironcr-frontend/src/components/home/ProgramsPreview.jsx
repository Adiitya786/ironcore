import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { programsApi, equipmentApi, trainersApi, galleryApi } from "../../services/api";

export default function ProgramsPreview() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    programsApi.getAll().then(data => setItems(data || [])).catch(() => setItems([]));
  }, []);

  const getImage = (p) => {
    if (p.imageData && p.imageType) return `data:${p.imageType};base64,${p.imageData}`;
    return "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=800&q=80";
  };

  return (
    <section className="section section-alt" aria-label="Our programs">
      <div className="section-inner">
        <h2 className="section-heading">Our Programs</h2>
        <div className="cards-grid cards-3">
          {items.map((p) => (
            <article className="program-card" key={p.id || p.title}>
              <Link to="/programs" className="program-image-link" aria-label={`View ${p.title}`}>
                <img src={getImage(p)} alt={p.title} loading="lazy" />
              </Link>
              <h3>{p.title}</h3>
              <p>{p.description?.substring(0, 100) || p.desc}</p>
              <Link to="/programs" className="btn-text">Learn More</Link>
            </article>
          ))}
        </div>
        {items.length === 0 && <p style={{textAlign:"center"}}>No programs saved by admin yet.</p>}
        <div className="section-cta">
          <Link to="/programs" className="btn-outline btn-md">VIEW ALL PROGRAMS</Link>
        </div>
      </div>
    </section>
  );
}
