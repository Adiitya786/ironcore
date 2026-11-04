import { useState } from "react";
import { programsApi } from "../services/api";

/**
 * Admin Program New Page - ADMIN ONLY
 * Allows admin to create a new program with image upload.
 */
export default function AdminProgramNew() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [benefits, setBenefits] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const formData = new FormData();
      formData.append("title", title);
      formData.append("description", description);
      formData.append("benefits", benefits);
      if (imageFile) formData.append("imageFile", imageFile);

      await programsApi.create(formData);
      setSuccess(true);
      setTitle("");
      setDescription("");
      setBenefits("");
      setImageFile(null);
    } catch (err) {
      setError(err.message || "Failed to create program.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <section className="page-hero">
        <div className="page-hero-inner"><h1>Add New Program</h1><p>Admin only — create a new fitness program.</p></div>
      </section>

      <section className="section">
        <div className="section-inner" style={{ maxWidth: "600px", margin: "0 auto" }}>
          {success && <div style={{ padding: "1rem", background: "#e8f5e9", color: "#2e7d32", borderRadius: "8px", marginBottom: "1rem" }}>Program created successfully!</div>}
          {error && <div className="error-message" style={{ marginBottom: "1rem" }}>{error}</div>}

          <form onSubmit={handleSubmit} aria-label="Create program form">
            <label htmlFor="progTitle">Program Title *</label>
            <input id="progTitle" value={title} onChange={e => setTitle(e.target.value)} placeholder="Weight Loss Program" required />

            <label htmlFor="progDesc" style={{ marginTop: "1rem" }}>Description</label>
            <textarea id="progDesc" rows={3} value={description} onChange={e => setDescription(e.target.value)} placeholder="Program details..." />

            <label htmlFor="progBenefits" style={{ marginTop: "1rem" }}>Benefits (comma-separated)</label>
            <input id="progBenefits" value={benefits} onChange={e => setBenefits(e.target.value)} placeholder="Calorie-focused, Progress tracking, Nutrition guidance" />

            <label htmlFor="progImage" style={{ marginTop: "1rem" }}>Program Image (optional)</label>
            <input id="progImage" type="file" accept="image/*" onChange={e => setImageFile(e.target.files[0])} />

            <button type="submit" className="btn-primary" disabled={loading} style={{ marginTop: "1.5rem" }}>
              {loading ? "CREATING..." : "CREATE PROGRAM"}
            </button>
          </form>
        </div>
      </section>
    </>
  );
}
