import { useState, useEffect } from "react";
import { equipmentApi, isAdmin } from "../services/api";

/**
 * Equipment Page - PUBLIC
 * Shows gym equipment with category filters.
 * Admin users can add/edit/delete equipment.
 */
export default function Equipment() {
  const [equipment, setEquipment] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeCategory, setActiveCategory] = useState("ALL");

  const categories = ["ALL", "STRENGTH", "CARDIO", "OTHER"];

  useEffect(() => {
    fetchEquipment();
  }, []);

  const fetchEquipment = async () => {
    try {
      setLoading(true);
      const data = await equipmentApi.getAll();
      setEquipment(data);
    } catch (err) {
      console.error("Error fetching equipment:", err);
    } finally {
      setLoading(false);
    }
  };

  const filteredEquipment = activeCategory === "ALL"
    ? equipment
    : equipment.filter(e => e.category === activeCategory);

  const getImageSrc = (item) => {
    if (item.imageData && item.imageType) {
      return `data:${item.imageType};base64,${item.imageData}`;
    }
    return "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600&q=80";
  };

  if (loading) {
    return (
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Equipment</h1>
          <p>Loading equipment...</p>
        </div>
      </section>
    );
  }

  return (
    <>
      <section className="page-hero">
        <div className="page-hero-inner">
          <h1>Our Equipment</h1>
          <p>State-of-the-art machines maintained daily for peak performance.</p>
        </div>
      </section>

      <section className="section">
        <div className="section-inner">
          {/* Category Filter Tabs */}
          <div className="filter-row" style={{ marginBottom: "2rem" }}>
            {categories.map(cat => (
              <button
                key={cat}
                className={`filter-btn ${activeCategory === cat ? "active" : ""}`}
                onClick={() => setActiveCategory(cat)}
              >
                {cat}
              </button>
            ))}
          </div>

          {/* Equipment Grid */}
          <div className="cards-grid cards-3">
            {filteredEquipment.map(item => (
              <article key={item.id} className="equipment-card">
                <img src={getImageSrc(item)} alt={item.name} loading="lazy" />
                <h3>{item.name}</h3>
                <p>{item.description}</p>
                <span className="category-badge">{item.category}</span>
              </article>
            ))}
          </div>

          {filteredEquipment.length === 0 && (
            <p style={{ textAlign: "center", padding: "2rem" }}>
              No equipment found in this category.
            </p>
          )}
        </div>
      </section>
    </>
  );
}
