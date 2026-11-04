import { useState } from "react";
import { trainersApi } from "../services/api";

export default function AdminTrainerNew() {
  const [name, setName] = useState("");
  const [bio, setBio] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("name", name);
      formData.append("bio", bio);
      if (imageFile) formData.append("imageFile", imageFile);
      await trainersApi.create(formData);
      setSuccess(true); setName(""); setBio(""); setImageFile(null);
    } catch (err) { alert("Failed: " + err.message); }
    finally { setLoading(false); }
  };

  return (
    <>
      <section className="page-hero"><div className="page-hero-inner"><h1>Add Trainer</h1></div></section>
      <section className="section">
        <div className="section-inner" style={{ maxWidth: "600px", margin: "0 auto" }}>
          {success && <p>Trainer added!</p>}
          <form onSubmit={handleSubmit} aria-label="Add trainer">
            <label>Trainer Name</label>
            <input value={name} onChange={e => setName(e.target.value)} required />
            <label>Bio</label>
            <textarea value={bio} onChange={e => setBio(e.target.value)} rows={3} />
            <label>Image (optional)</label>
            <input type="file" accept="image/*" onChange={e => setImageFile(e.target.files[0])} />
            <button type="submit" className="btn-primary" disabled={loading}>{loading ? "CREATING..." : "CREATE TRAINER"}</button>
          </form>
        </div>
      </section>
    </>
  );
}
