import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { galleryApi } from "../../services/api";

export default function GalleryPreview() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    galleryApi.getAll().then(data => setItems(data || [])).catch(() => setItems([]));
  }, []);

  const getImage = (g) => {
    if (g.imageData && g.imageType) return `data:${g.imageType};base64,${g.imageData}`;
    return "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800&q=80";
  };

  return (
    <section className="section section-alt" aria-label="Gallery preview">
      <div className="section-inner">
        <h2 className="section-heading">Gallery</h2>
        <div className="cards-grid cards-3">
          {items.slice(0, 3).map((g) => (
            <article key={g.id || g.title} className="gallery-card">
              <img src={getImage(g)} alt={g.title || "Gallery"} loading="lazy" />
              <h3>{g.title || "Image"}</h3>
              <p>{g.description?.substring(0, 80) || ""}</p>
            </article>
          ))}
        </div>
        {items.length === 0 && <p style={{textAlign:"center"}}>No gallery images saved by admin yet.</p>}
        <div className="section-cta">
          <Link to="/gallery" className="btn-outline btn-md">VIEW ALL</Link>
        </div>
      </div>
    </section>
  );
}
