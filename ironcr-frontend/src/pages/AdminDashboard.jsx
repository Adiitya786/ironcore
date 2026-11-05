import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import {
  programsApi,
  equipmentApi,
  trainersApi,
  galleryApi,
  membershipApi,
  contactApi,
  trialsApi,
  isAdmin,
  logout
} from "../services/api";

/**
 * Comprehensive Admin Dashboard — ADMIN ONLY
 * Full CRUD control over Programs, Equipment, Trainers, Gallery, Memberships, and Contact messages.
 */
export default function AdminDashboard() {
  const [activeTab, setActiveTab] = useState("programs");

  // Data states
  const [programs, setPrograms] = useState([]);
  const [equipment, setEquipment] = useState([]);
  const [trainers, setTrainers] = useState([]);
  const [gallery, setGallery] = useState([]);
  const [memberships, setMemberships] = useState([]);
  const [contacts, setContacts] = useState([]);
  const [trials, setTrials] = useState([]);
  const [loading, setLoading] = useState(true);

  // Edit / Create modal states
  const [editingItem, setEditingItem] = useState(null); // null, or object being edited
  const [isCreating, setIsCreating] = useState(false);
  const [formData, setFormData] = useState({});
  const [imageFile, setImageFile] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (!isAdmin()) {
      window.location.href = "/";
      return;
    }
    fetchAllData();
  }, []);

  const fetchAllData = async () => {
    try {
      setLoading(true);
      const [pData, eData, tData, gData, mData, cData, trData] = await Promise.all([
        programsApi.getAll().catch(() => []),
        equipmentApi.getAll().catch(() => []),
        trainersApi.getAll().catch(() => []),
        galleryApi.getAll().catch(() => []),
        membershipApi.getAll().catch(() => []),
        contactApi.getAll().catch(() => []),
        trialsApi.getAll().catch(() => [])
      ]);
      setPrograms(pData);
      setEquipment(eData);
      setTrainers(tData);
      setGallery(gData);
      setMemberships(mData);
      setContacts(cData);
      setTrials(trData);
    } catch (err) {
      console.error("Error loading admin data:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    window.location.href = "/login";
  };

  // Generic Delete Handler
  const handleDelete = async (type, id) => {
    if (!confirm(`Are you sure you want to delete this ${type.slice(0, -1)}?`)) return;
    try {
      if (type === "programs") await programsApi.delete(id);
      else if (type === "equipment") await equipmentApi.delete(id);
      else if (type === "trainers") await trainersApi.delete(id);
      else if (type === "gallery") await galleryApi.delete(id);
      else if (type === "memberships") await membershipApi.delete(id);
      else if (type === "contacts") await contactApi.delete(id);
      else if (type === "trials") await trialsApi.delete(id);

      fetchAllData();
    } catch (err) {
      alert("Delete failed: " + err.message);
    }
  };

  // Open Edit Modal
  const openEdit = (item, type) => {
    setEditingItem({ ...item, type });
    setFormData({ ...item });
    setImageFile(null);
    setIsCreating(false);
  };

  // Open Create Modal
  const openCreate = (type) => {
    setEditingItem(null);
    setIsCreating(true);
    setFormData({ type, category: type === "equipment" ? "STRENGTH" : type === "gallery" ? "GYM" : undefined });
    setImageFile(null);
  };

  // Handle Form Submit (Create or Update)
  const handleSave = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const type = isCreating ? formData.type : editingItem.type;
      const id = isCreating ? null : editingItem.id;

      const data = new FormData();
      Object.keys(formData).forEach(key => {
        if (key !== 'type' && formData[key] !== undefined && formData[key] !== null) {
          data.append(key, formData[key]);
        }
      });
      if (imageFile) {
        data.append("imageFile", imageFile);
      }

      if (type !== "memberships" && type !== "contacts" && isCreating && !formData.title && !formData.name && !formData.imageFile) {
        // nothing to fix here; just continue
      }

      if (isCreating) {
        if (type === "programs") await programsApi.create(data);
        else if (type === "equipment") await equipmentApi.create(data);
        else if (type === "trainers") await trainersApi.create(data);
        else if (type === "gallery") await galleryApi.create(data);
      } else {
        if (type === "programs") await programsApi.update(id, data);
        else if (type === "equipment") await equipmentApi.update(id, data);
        else if (type === "trainers") await trainersApi.update(id, data);
        else if (type === "gallery") await galleryApi.update(id, data);
      }

      setEditingItem(null);
      setIsCreating(false);
      fetchAllData();
    } catch (err) {
      alert("Operation failed: " + err.message);
    } finally {
      setSubmitting(false);
    }
  };

  // Status updaters for Membership & Contact
  const updateMembershipStatus = async (id, status) => {
    try {
      await membershipApi.updateStatus(id, status);
      fetchAllData();
    } catch (err) { alert("Failed: " + err.message); }
  };

  const updateContactStatus = async (id, status) => {
    try {
      await contactApi.updateStatus(id, status);
      fetchAllData();
    } catch (err) { alert("Failed: " + err.message); }
  };

  const updateTrialStatus = async (id, status) => {
    try {
      await trialsApi.updateStatus(id, status);
      fetchAllData();
    } catch (err) { alert("Failed: " + err.message); }
  };

  if (loading) {
    return (
      <div style={{ padding: "100px 24px", textAlign: "center" }}>
        <h2>Loading Admin Dashboard...</h2>
      </div>
    );
  }

  return (
    <div className="admin-dashboard">
      <style>{`
        :root {
          --admin-bg: #f4f6f9;
          --admin-surface: #ffffff;
          --admin-primary: #1a2028;
          --admin-accent: #c89f5e;
          --admin-border: #e2e8f0;
          --admin-text: #2d3748;
          --admin-text-secondary: #718096;
        }
        .admin-page { background: var(--admin-bg); min-height: 100vh; padding-bottom: 80px; }
        .admin-hero { background: linear-gradient(135deg, #1a2028 0%, #2d3748 100%); color: #fff; padding: 60px 24px; }
        .admin-hero-inner { max-width: 1280px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 20px; }
        .admin-hero h1 { font-size: 2rem; font-weight: 800; margin: 0; }
        .admin-hero p { opacity: 0.8; margin: 4px 0 0; font-size: 0.95rem; }

        .admin-container { max-width: 1280px; margin: 32px auto 0; padding: 0 24px; }

        /* Tabs */
        .admin-tabs { display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 24px; border-bottom: 2px solid var(--admin-border); padding-bottom: 12px; }
        .admin-tab { padding: 10px 20px; border-radius: 8px; background: var(--admin-surface); border: 1.5px solid var(--admin-border); font-weight: 700; font-size: 0.85rem; cursor: pointer; transition: all 0.2s; color: var(--admin-text); }
        .admin-tab:hover { border-color: var(--admin-accent); color: var(--admin-accent); }
        .admin-tab.active { background: var(--admin-primary); color: #fff; border-color: var(--admin-primary); }

        /* Card container */
        .admin-card { background: var(--admin-surface); border: 1px solid var(--admin-border); border-radius: 12px; padding: 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.04); }
        .admin-header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
        .admin-header-row h2 { font-size: 1.25rem; font-weight: 800; color: var(--admin-text); margin: 0; }

        /* Tables */
        .admin-table-wrap { overflow-x: auto; }
        .admin-table { width: 100%; border-collapse: collapse; text-align: left; font-size: 0.9rem; }
        .admin-table th { background: #f8fafc; color: var(--admin-text-secondary); font-weight: 700; padding: 12px 16px; border-bottom: 2px solid var(--admin-border); text-transform: uppercase; font-size: 0.75rem; letter-spacing: 0.05em; }
        .admin-table td { padding: 14px 16px; border-bottom: 1px solid var(--admin-border); color: var(--admin-text); vertical-align: middle; }
        .admin-table tr:hover td { background: #f8fafc; }

        /* Buttons & Actions */
        .btn-action { padding: 6px 12px; border-radius: 6px; font-weight: 700; font-size: 0.75rem; cursor: pointer; border: 1.5px solid var(--admin-border); background: #fff; transition: all 0.15s; margin-right: 6px; text-transform: uppercase; }
        .btn-action:hover { background: #f1f5f9; }
        .btn-action.edit { color: #2b6cb0; border-color: #bee3f8; }
        .btn-action.edit:hover { background: #ebf8ff; }
        .btn-action.delete { color: #c53030; border-color: #fed7d7; }
        .btn-action.delete:hover { background: #fff5f5; }
        .btn-action.primary { background: var(--admin-accent); color: #fff; border-color: var(--admin-accent); }
        .btn-action.primary:hover { opacity: 0.9; }

        .thumb-img { width: 48px; height: 48px; object-fit: cover; border-radius: 6px; border: 1px solid var(--admin-border); }

        /* Modal */
        .modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); backdrop-filter: blur(4px); display: grid; place-items: center; z-index: 1000; padding: 24px; }
        .modal-box { background: #fff; border-radius: 12px; padding: 32px; width: 100%; max-width: 600px; max-height: 90vh; overflow-y: auto; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1); }
        .modal-box h3 { font-size: 1.3rem; font-weight: 800; margin-top: 0; margin-bottom: 20px; color: var(--admin-primary); }
        .modal-field { display: flex; flex-direction: column; gap: 6px; margin-bottom: 16px; }
        .modal-field label { font-size: 0.8rem; font-weight: 700; text-transform: uppercase; color: var(--admin-text-secondary); }
        .modal-field input, .modal-field textarea, .modal-field select { padding: 10px 14px; border: 1.5px solid var(--admin-border); border-radius: 8px; font-family: inherit; font-size: 0.95rem; outline: none; }
        .modal-field input:focus, .modal-field textarea:focus, .modal-field select:focus { border-color: var(--admin-accent); }
        .modal-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px; }
      `}</style>

      <div className="admin-page">
        <div className="admin-hero">
          <div className="admin-hero-inner">
            <div>
              <h1>Admin Management Dashboard</h1>
              <p>Full CRUD control over programs, equipment, trainers, gallery, memberships, contacts, and trial requests.</p>
            </div>
            <button onClick={handleLogout} className="btn-action" style={{ background: "rgba(255,255,255,0.1)", color: "#fff", border: "1px solid rgba(255,255,255,0.2)" }}>
              Logout
            </button>
          </div>
        </div>

        <div className="admin-container">
          {/* Navigation Tabs */}
          <div className="admin-tabs">
            <button className={`admin-tab${activeTab === "programs" ? " active" : ""}`} onClick={() => setActiveTab("programs")}>
              Programs ({programs.length})
            </button>
            <button className={`admin-tab${activeTab === "equipment" ? " active" : ""}`} onClick={() => setActiveTab("equipment")}>
              Equipment ({equipment.length})
            </button>
            <button className={`admin-tab${activeTab === "trainers" ? " active" : ""}`} onClick={() => setActiveTab("trainers")}>
              Trainers ({trainers.length})
            </button>
            <button className={`admin-tab${activeTab === "gallery" ? " active" : ""}`} onClick={() => setActiveTab("gallery")}>
              Gallery ({gallery.length})
            </button>
            <button className={`admin-tab${activeTab === "memberships" ? " active" : ""}`} onClick={() => setActiveTab("memberships")}>
              Memberships ({memberships.length})
            </button>
            <button className={`admin-tab${activeTab === "contacts" ? " active" : ""}`} onClick={() => setActiveTab("contacts")}>
              Contacts ({contacts.length})
            </button>
            <button className={`admin-tab${activeTab === "trials" ? " active" : ""}`} onClick={() => setActiveTab("trials")}>
              Trials ({trials.length})
            </button>
          </div>

          {/* TAB 1: PROGRAMS */}
          {activeTab === "programs" && (
            <div className="admin-card">
              <div className="admin-header-row">
                <h2>Programs Management</h2>
                <button className="btn-action primary" onClick={() => openCreate("programs")}>+ Add Program</button>
              </div>
              <div className="admin-table-wrap">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>Image</th>
                      <th>Title</th>
                      <th>Description</th>
                      <th>Benefits</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {programs.map(p => (
                      <tr key={p.id}>
                        <td>
                          {p.imageName ? (
                            <img src={`data:${p.imageType || 'image/jpeg'};base64,${p.imageData}`} alt={p.title} className="thumb-img" />
                          ) : (
                            <div className="thumb-img" style={{ background: "#edf2f7", display: "grid", placeItems: "center", fontSize: "0.7rem", color: "#a0aec0" }}>No Image</div>
                          )}
                        </td>
                        <td><strong>{p.title}</strong></td>
                        <td>{p.description?.substring(0, 60)}...</td>
                        <td>{p.benefits}</td>
                        <td>
                          <button className="btn-action edit" onClick={() => openEdit(p, "programs")}>Edit</button>
                          <button className="btn-action delete" onClick={() => handleDelete("programs", p.id)}>Delete</button>
                        </td>
                      </tr>
                    ))}
                    {programs.length === 0 && <tr><td colSpan="5" style={{ textAlign: "center", padding: "24px" }}>No programs found.</td></tr>}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 2: EQUIPMENT */}
          {activeTab === "equipment" && (
            <div className="admin-card">
              <div className="admin-header-row">
                <h2>Equipment Management</h2>
                <button className="btn-action primary" onClick={() => openCreate("equipment")}>+ Add Equipment</button>
              </div>
              <div className="admin-table-wrap">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>Image</th>
                      <th>Name</th>
                      <th>Category</th>
                      <th>Description</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {equipment.map(e => (
                      <tr key={e.id}>
                        <td>
                          {e.imageName ? (
                            <img src={`data:${e.imageType || 'image/jpeg'};base64,${e.imageData}`} alt={e.name} className="thumb-img" />
                          ) : (
                            <div className="thumb-img" style={{ background: "#edf2f7", display: "grid", placeItems: "center", fontSize: "0.7rem", color: "#a0aec0" }}>No Image</div>
                          )}
                        </td>
                        <td><strong>{e.name}</strong></td>
                        <td><span style={{ background: "#e2e8f0", padding: "2px 8px", borderRadius: "4px", fontSize: "0.75rem", fontWeight: "700" }}>{e.category}</span></td>
                        <td>{e.description?.substring(0, 60)}...</td>
                        <td>
                          <button className="btn-action edit" onClick={() => openEdit(e, "equipment")}>Edit</button>
                          <button className="btn-action delete" onClick={() => handleDelete("equipment", e.id)}>Delete</button>
                        </td>
                      </tr>
                    ))}
                    {equipment.length === 0 && <tr><td colSpan="5" style={{ textAlign: "center", padding: "24px" }}>No equipment found.</td></tr>}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 3: TRAINERS */}
          {activeTab === "trainers" && (
            <div className="admin-card">
              <div className="admin-header-row">
                <h2>Trainers Management</h2>
                <button className="btn-action primary" onClick={() => openCreate("trainers")}>+ Add Trainer</button>
              </div>
              <div className="admin-table-wrap">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>Photo</th>
                      <th>Name</th>
                      <th>Bio</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {trainers.map(t => (
                      <tr key={t.id}>
                        <td>
                          {t.imageName ? (
                            <img src={`data:${t.imageType || 'image/jpeg'};base64,${t.imageData}`} alt={t.name} className="thumb-img" />
                          ) : (
                            <div className="thumb-img" style={{ background: "#edf2f7", display: "grid", placeItems: "center", fontSize: "0.7rem", color: "#a0aec0" }}>No Photo</div>
                          )}
                        </td>
                        <td><strong>{t.name}</strong></td>
                        <td>{t.bio?.substring(0, 80)}...</td>
                        <td>
                          <button className="btn-action edit" onClick={() => openEdit(t, "trainers")}>Edit</button>
                          <button className="btn-action delete" onClick={() => handleDelete("trainers", t.id)}>Delete</button>
                        </td>
                      </tr>
                    ))}
                    {trainers.length === 0 && <tr><td colSpan="4" style={{ textAlign: "center", padding: "24px" }}>No trainers found.</td></tr>}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 4: GALLERY */}
          {activeTab === "gallery" && (
            <div className="admin-card">
              <div className="admin-header-row">
                <h2>Gallery Management</h2>
                <button className="btn-action primary" onClick={() => openCreate("gallery")}>+ Upload Image</button>
              </div>
              <div className="admin-table-wrap">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>Image</th>
                      <th>Title</th>
                      <th>Category</th>
                      <th>Description</th>
                      <th>Order</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {gallery.map(g => (
                      <tr key={g.id}>
                        <td>
                          {g.imageName ? (
                            <img src={`data:${g.imageType || 'image/jpeg'};base64,${g.imageData}`} alt={g.title} className="thumb-img" />
                          ) : (
                            <div className="thumb-img" style={{ background: "#edf2f7", display: "grid", placeItems: "center", fontSize: "0.7rem", color: "#a0aec0" }}>No Image</div>
                          )}
                        </td>
                        <td><strong>{g.title}</strong></td>
                        <td><span style={{ background: "#e2e8f0", padding: "2px 8px", borderRadius: "4px", fontSize: "0.75rem", fontWeight: "700" }}>{g.category}</span></td>
                        <td>{g.description?.substring(0, 50)}...</td>
                        <td>{g.displayOrder}</td>
                        <td>
                          <button className="btn-action edit" onClick={() => openEdit(g, "gallery")}>Edit</button>
                          <button className="btn-action delete" onClick={() => handleDelete("gallery", g.id)}>Delete</button>
                        </td>
                      </tr>
                    ))}
                    {gallery.length === 0 && <tr><td colSpan="6" style={{ textAlign: "center", padding: "24px" }}>No gallery images found.</td></tr>}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 5: MEMBERSHIPS */}
          {activeTab === "memberships" && (
            <div className="admin-card">
              <div className="admin-header-row">
                <h2>Membership Requests Management</h2>
              </div>
              <div className="admin-table-wrap">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Phone / Email</th>
                      <th>Plan</th>
                      <th>Status</th>
                      <th>Status Actions</th>
                      <th>Delete</th>
                    </tr>
                  </thead>
                  <tbody>
                    {memberships.map(m => (
                      <tr key={m.id}>
                        <td><strong>{m.fullName}</strong></td>
                        <td>{m.phone}<br/><span style={{ fontSize: "0.8rem", color: "#718096" }}>{m.email}</span></td>
                        <td><strong>{m.plan}</strong> ({m.billing})</td>
                        <td>
                          <span style={{
                            background: m.status === "CONFIRMED" ? "#c6f6d5" : m.status === "REJECTED" ? "#fed7d7" : "#feebc8",
                            color: m.status === "CONFIRMED" ? "#22543d" : m.status === "REJECTED" ? "#742a2a" : "#744210",
                            padding: "4px 10px", borderRadius: "4px", fontSize: "0.75rem", fontWeight: "700"
                          }}>
                            {m.status}
                          </span>
                        </td>
                        <td>
                          <button className="btn-action" onClick={() => updateMembershipStatus(m.id, "CONFIRMED")}>Confirm</button>
                          <button className="btn-action" onClick={() => updateMembershipStatus(m.id, "CONTACTED")}>Contacted</button>
                          <button className="btn-action" onClick={() => updateMembershipStatus(m.id, "REJECTED")}>Reject</button>
                        </td>
                        <td>
                          <button className="btn-action delete" onClick={() => handleDelete("memberships", m.id)}>Delete</button>
                        </td>
                      </tr>
                    ))}
                    {memberships.length === 0 && <tr><td colSpan="6" style={{ textAlign: "center", padding: "24px" }}>No membership requests found.</td></tr>}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 6: CONTACT MESSAGES */}
          {activeTab === "contacts" && (
            <div className="admin-card">
              <div className="admin-header-row">
                <h2>Contact Messages Management</h2>
              </div>
              <div className="admin-table-wrap">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>From</th>
                      <th>Subject</th>
                      <th>Message</th>
                      <th>Status</th>
                      <th>Status Actions</th>
                      <th>Delete</th>
                    </tr>
                  </thead>
                  <tbody>
                    {contacts.map(c => (
                      <tr key={c.id}>
                        <td><strong>{c.name}</strong><br/><span style={{ fontSize: "0.8rem", color: "#718096" }}>{c.phone} | {c.email}</span></td>
                        <td>{c.subject || "General"}</td>
                        <td>{c.message?.substring(0, 60)}...</td>
                        <td>
                          <span style={{
                            background: c.status === "RESPONDED" ? "#c6f6d5" : c.status === "READ" ? "#bee3f8" : "#feebc8",
                            color: c.status === "RESPONDED" ? "#22543d" : c.status === "READ" ? "#2a4365" : "#744210",
                            padding: "4px 10px", borderRadius: "4px", fontSize: "0.75rem", fontWeight: "700"
                          }}>
                            {c.status}
                          </span>
                        </td>
                        <td>
                          <button className="btn-action" onClick={() => updateContactStatus(c.id, "READ")}>Read</button>
                          <button className="btn-action" onClick={() => updateContactStatus(c.id, "RESPONDED")}>Responded</button>
                          <button className="btn-action" onClick={() => updateContactStatus(c.id, "CLOSED")}>Close</button>
                        </td>
                        <td>
                          <button className="btn-action delete" onClick={() => handleDelete("contacts", c.id)}>Delete</button>
                        </td>
                      </tr>
                    ))}
                    {contacts.length === 0 && <tr><td colSpan="6" style={{ textAlign: "center", padding: "24px" }}>No contact messages found.</td></tr>}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 7: TRIALS */}
          {activeTab === "trials" && (
            <div className="admin-card">
              <div className="admin-header-row">
                <h2>Trial Requests Management</h2>
              </div>
              <div className="admin-table-wrap">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Phone / Email</th>
                      <th>Preferred Date</th>
                      <th>Preferred Time</th>
                      <th>Fitness Goal</th>
                      <th>Experience Level</th>
                      <th>Message</th>
                      <th>Status</th>
                      <th>Status Actions</th>
                      <th>Delete</th>
                    </tr>
                  </thead>
                  <tbody>
                    {trials.length > 0 ? (
                      trials.map(t => (
                        <tr key={t.id}>
                          <td><strong>{t.fullName}</strong></td>
                          <td>{t.phone}<br/><span style={{ fontSize: "0.8rem", color: "#718096" }}>{t.email}</span></td>
                          <td>{t.preferredDate}</td>
                          <td>{t.preferredTime}</td>
                          <td>{t.fitnessGoal}</td>
                          <td>{t.experience}</td>
                          <td>{t.message}</td>
                          <td>
                            <span style={{
                              background: t.status === "CONFIRMED" ? "#c6f6d5" : t.status === "REJECTED" ? "#fed7d7" : t.status === "CONTACTED" ? "#feebc8" : "#feebc8",
                              color: t.status === "CONFIRMED" ? "#22543d" : t.status === "REJECTED" ? "#742a2a" : "#744210",
                              padding: "4px 10px", borderRadius: "4px", fontSize: "0.75rem", fontWeight: "700"
                            }}>
                              {t.status}
                            </span>
                          </td>
                          <td>
                            <button className="btn-action" onClick={() => updateTrialStatus(t.id, "CONFIRMED")}>Confirm</button>
                            <button className="btn-action" onClick={() => updateTrialStatus(t.id, "REJECTED")}>Reject</button>
                            <button className="btn-action" onClick={() => updateTrialStatus(t.id, "CONTACTED")}>Contacted</button>
                          </td>
                          <td>
                            <button className="btn-action delete" onClick={() => handleDelete("trials", t.id)}>Delete</button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr><td colSpan="10" style={{ textAlign: "center", padding: "24px" }}>No trial requests found.</td></tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          )}

        </div>
      </div>

      {/* EDIT / CREATE MODAL */}
      {(editingItem || isCreating) && (
        <div className="modal-overlay">
          <div className="modal-box">
            <h3>{isCreating ? `Add New ${isCreating ? (formData.type || activeTab).slice(0, -1) : ''}` : `Edit Item`}</h3>
            <form onSubmit={handleSave}>

              {/* PROGRAMS FIELDS */}
              {((isCreating && formData.type === "programs") || (!isCreating && editingItem?.type === "programs")) && (
                <>
                  <div className="modal-field">
                    <label>Title *</label>
                    <input value={formData.title || ""} onChange={e => setFormData({ ...formData, title: e.target.value })} required />
                  </div>
                  <div className="modal-field">
                    <label>Description</label>
                    <textarea rows={3} value={formData.description || ""} onChange={e => setFormData({ ...formData, description: e.target.value })} />
                  </div>
                  <div className="modal-field">
                    <label>Benefits (comma separated)</label>
                    <input value={formData.benefits || ""} onChange={e => setFormData({ ...formData, benefits: e.target.value })} />
                  </div>
                </>
              )}

              {/* EQUIPMENT FIELDS */}
              {((isCreating && formData.type === "equipment") || (!isCreating && editingItem?.type === "equipment")) && (
                <>
                  <div className="modal-field">
                    <label>Equipment Name *</label>
                    <input value={formData.name || ""} onChange={e => setFormData({ ...formData, name: e.target.value })} required />
                  </div>
                  <div className="modal-field">
                    <label>Category</label>
                    <select value={formData.category || "STRENGTH"} onChange={e => setFormData({ ...formData, category: e.target.value })}>
                      <option value="STRENGTH">STRENGTH</option>
                      <option value="CARDIO">CARDIO</option>
                      <option value="OTHER">OTHER</option>
                    </select>
                  </div>
                  <div className="modal-field">
                    <label>Description</label>
                    <textarea rows={3} value={formData.description || ""} onChange={e => setFormData({ ...formData, description: e.target.value })} />
                  </div>
                </>
              )}

              {/* TRAINERS FIELDS */}
              {((isCreating && formData.type === "trainers") || (!isCreating && editingItem?.type === "trainers")) && (
                <>
                  <div className="modal-field">
                    <label>Trainer Name *</label>
                    <input value={formData.name || ""} onChange={e => setFormData({ ...formData, name: e.target.value })} required />
                  </div>
                  <div className="modal-field">
                    <label>Bio / Expertise</label>
                    <textarea rows={3} value={formData.bio || ""} onChange={e => setFormData({ ...formData, bio: e.target.value })} />
                  </div>
                </>
              )}

              {/* GALLERY FIELDS */}
              {((isCreating && formData.type === "gallery") || (!isCreating && editingItem?.type === "gallery")) && (
                <>
                  <div className="modal-field">
                    <label>Title *</label>
                    <input value={formData.title || ""} onChange={e => setFormData({ ...formData, title: e.target.value })} required />
                  </div>
                  <div className="modal-field">
                    <label>Category</label>
                    <select value={formData.category || "GYM"} onChange={e => setFormData({ ...formData, category: e.target.value })}>
                      <option value="GYM">GYM</option>
                      <option value="EQUIPMENT">EQUIPMENT</option>
                      <option value="TRAINERS">TRAINERS</option>
                      <option value="MEMBERS">MEMBERS</option>
                      <option value="EVENTS">EVENTS</option>
                    </select>
                  </div>
                  <div className="modal-field">
                    <label>Description</label>
                    <textarea rows={2} value={formData.description || ""} onChange={e => setFormData({ ...formData, description: e.target.value })} />
                  </div>
                  <div className="modal-field">
                    <label>Display Order</label>
                    <input type="number" value={formData.displayOrder || 0} onChange={e => setFormData({ ...formData, displayOrder: parseInt(e.target.value) || 0 })} />
                  </div>
                </>
              )}

              {/* IMAGE UPLOAD FOR ALL */}
              <div className="modal-field">
                <label>Image File {isCreating ? "(Required)" : "(Replace optional)"}</label>
                <input type="file" accept="image/*" onChange={e => setImageFile(e.target.files[0])} />
              </div>

              <div className="modal-actions">
                <button type="button" className="btn-action" onClick={() => { setEditingItem(null); setIsCreating(false); }}>Cancel</button>
                <button type="submit" className="btn-action primary" disabled={submitting}>
                  {submitting ? "Saving..." : "Save Changes"}
                </button>
              </div>

            </form>
          </div>
        </div>
      )}

    </div>
  );
}
