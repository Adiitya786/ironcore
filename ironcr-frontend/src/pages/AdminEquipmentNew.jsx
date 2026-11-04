import { useState } from "react";
import { equipmentApi } from "../services/api";

export default function AdminEquipmentNew() {
  const [name, setName] = useState("");
  const [category, setCategory] = useState("STRENGTH");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("name", name);
      formData.append("category", category);
      formData.append("description", description);
      if (imageFile) formData.append("imageFile", imageFile);
      await equipmentApi.create(formData);
      setSuccess(true);
      setName("");
      setCategory("STRENGTH");
      setDescription("");
      setImageFile(null);
    } catch (err) {
      alert("Failed: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <section className="page-hero"><div className="page-hero-inner"><h1>Add Equipment</h1></div></section>
      <section className="section">
        <div className="section-inner" style={{ maxWidth: "600px", margin: "0 auto" }}>
          {success && <p>Equipment added!</p>}
          <form onSubmit={handleSubmit} aria-label="Add equipment">
            <label>Equipment Name</label>
            <input value={name} onChange={e => setName(e.target.value)} required />
            <label>Category</label>
            <select value={category} onChange={e => setCategory(e.target.value)}>
              <option>STRENGTH</option>
              <option>CARDIO</option>
              <option>OTHER</option>
            </select>
            <label>Description</label>
            <textarea value={description} onChange={e => setDescription(e.target.value)} rows={3} />
            <label>Image (optional)</label>
            <input type="file" accept="image/*" onChange={e => setImageFile(e.target.files[0])} />
            <button type="submit" className="btn-primary" disabled={loading}>{loading ? "CREATING..." : "CREATE EQUIPMENT"}</button>
          </form>
        </div>
      </section>
    </>
  );
}
