import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { equipmentApi } from "../../services/api";

export default function EquipmentPreview() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    equipmentApi.getAll().then(data => setItems(data || [])).catch(() => setItems([]));
  }, []);

  const getImage = (item) => {
    if (item.imageData && item.imageType) return `data:${item.imageType};base64,${item.imageData}`;
    return "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600&q=80";
  };

  return (
    <section className="section" aria-label="Equipment preview">
      <div className="section-inner">
        <h2 className="section-heading">Our Equipment</h2>
        <div className="cards-grid cards-3">
          {items.slice(0, 3).map((item) => (
            <article key={item.id || item.name} className="equipment-card">
              <img src={getImage(item)} alt={item.name} loading="lazy" />
              <h3>{item.name}</h3>
              <p>{item.description}</p>
              <span className="category-badge">{item.category}</span>
            </article>
          ))}
        </div>
        {items.length === 0 && <p style={{textAlign:"center"}}>No equipment saved by admin yet.</p>}
        <div className="section-cta">
          <Link to="/equipment" className="btn-outline btn-md">VIEW ALL EQUIPMENT</Link>
        </div>
      </div>
    </section>
  );
}
