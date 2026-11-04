import { useState } from "react";
import { galleryApi } from "../services/api";

export default function AdminGalleryNew() {
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("GYM");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [displayOrder, setDisplayOrder] = useState(0);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("title", title);
      formData.append("category", category);
      formData.append("description", description);
      formData.append("displayOrder", displayOrder);
      if (imageFile) formData.append("imageFile", imageFile);
      await galleryApi.create(formData);
      setSuccess(true); setTitle(""); setCategory("GYM"); setDescription(""); setImageFile(null); setDisplayOrder(0);
    } catch (err) { alert("Failed: " + err.message); }
    finally { setLoading(false); }
  };

  return (
    <>
      <section className="page-hero"><div className="page-hero-inner"><h1>Add Gallery Image</h1></div></section>
      <section className="section">
        <div className="section-inner" style={{ maxWidth: "600px", margin: "0 auto" }}>
          {success && <p>Image uploaded!</p>}
          <form onSubmit={handleSubmit} aria-label="Add gallery image">
            <label>Title</label>
            <input value={title} onChange={e => setTitle(e.target.value)} required />
            <label>Category</label>
            <select value={category} onChange={e => setCategory(e.target.value)}>
              <option>GYM</option><option>EQUIPMENT</option><option>TRAINERS</option><option>MEMBERS</option><option>EVENTS</option>
            </select>
            <label>Description</label>
            <textarea value={description} onChange={e => setDescription(e.target.value)} rows={3} />
            <label>Display Order</label>
            <input type="number" value={displayOrder} onChange={e => setDisplayOrder(parseInt(e.target.value) || 0)} />
            <label>Image</label>
            <input type="file" accept="image/*" onChange={e => setImageFile(e.target.files[0])} required />
            <button type="submit" className="btn-primary" disabled={loading}>{loading ? "UPLOADING..." : "UPLOAD IMAGE"}</button>
          </form>
        </div>
      </section>
    </>
  );
}
