import { useState, useEffect } from "react";
import { X, ChevronLeft, ChevronRight } from "lucide-react";
import { galleryApi } from "../services/api";

export default function Gallery() {
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [lightbox, setLightbox] = useState(null);
  const [filter, setFilter] = useState("All");

  const cats = ["All", "GYM", "EQUIPMENT", "TRAINERS", "MEMBERS", "EVENTS"];

  useEffect(() => {
    setLoading(true);
    galleryApi.getAll()
      .then(data => {
        setImages(data || []);
      })
      .catch(err => {
        console.error("Gallery fetch error:", err);
        setImages([]);
      })
      .finally(() => setLoading(false));
  }, []);

  const filtered = filter === "All" ? images : images.filter(g => g.category === filter);

  const open = (i) => setLightbox({ idx: i, list: filtered });
  const close = () => setLightbox(null);
  const prev = () => setLightbox(s => ({ ...s, idx: s.idx === 0 ? s.list.length - 1 : s.idx - 1 }));
  const next = () => setLightbox(s => ({ ...s, idx: s.idx === s.list.length - 1 ? 0 : s.idx + 1 }));

  return (
    <>
      <section className="page-hero"><div className="page-hero-inner"><h1>Gallery</h1><p>Inside IronCore Fitness — captured by our admin.</p></div></section>
      <section className="section"><div className="section-inner">
        <div className="filter-row">
          {cats.map((c) => (
            <button key={c} className={`filter-btn ${filter === c ? "active" : ""}`} onClick={() => setFilter(c)} aria-pressed={filter === c}>
              {c.charAt(0) + c.slice(1).toLowerCase()}
            </button>
          ))}
        </div>
        {loading ? (
          <div style={{ textAlign: "center", padding: "40px", color: "#a0aec0" }}>Loading gallery images...</div>
        ) : (
          <div className="gallery-grid gallery-3">
            {filtered.length === 0 ? (
              <p style={{ textAlign: "center", color: "#a0aec0", padding: "24px 0" }}>
                {images.length === 0 ? "No gallery images added yet. Admin can upload them from the Admin Dashboard." : `No images found for category: ${filter}`}
              </p>
            ) : (
              filtered.map((img, i) => (
                <button key={img.id || img.title} className="gallery-item" onClick={() => open(i)} aria-label={`Open ${img.category} image`}>
                  <img src={img.imageData ? `data:${img.imageType || 'image/jpeg'};base64,${img.imageData}` : img.imageUrl || img.src} alt={img.title || img.category} loading="lazy" />
                </button>
              ))
            )}
          </div>
        )}
      </div></section>

      {lightbox && lightbox.list.length > 0 && (
        <div className="lightbox" role="dialog" aria-modal="true" aria-label="Image lightbox">
          <button className="lightbox-close" onClick={close} aria-label="Close"><X size={28} /></button>
          <button className="lightbox-nav lightbox-prev" onClick={prev} aria-label="Previous"><ChevronLeft size={28} /></button>
          <img src={lightbox.list[lightbox.idx].imageData ? `data:${lightbox.list[lightbox.idx].imageType || 'image/jpeg'};base64,${lightbox.list[lightbox.idx].imageData}` : lightbox.list[lightbox.idx].imageUrl || lightbox.list[lightbox.idx].src} alt={lightbox.list[lightbox.idx].title || lightbox.list[lightbox.idx].category} />
          <button className="lightbox-nav lightbox-next" onClick={next} aria-label="Next"><ChevronRight size={28} /></button>
        </div>
      )}
    </>
  );
}
