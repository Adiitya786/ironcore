import { useState } from "react";
import { contactApi } from "../services/api";

const contactDetails = [
  {
    icon: "📞",
    label: "Phone",
    value: "+91 98765 43210",
    sub: "Mon–Sat, 8AM–8PM"
  },
  {
    icon: "✉️",
    label: "Email",
    value: "hello@ironcorefitness.com",
    sub: "Reply within 24 hours"
  },
  {
    icon: "📍",
    label: "Address",
    value: "Sector 12, Near City Mall",
    sub: "Gurugram, Haryana 122001"
  },
  {
    icon: "🕐",
    label: "Hours",
    value: "Mon–Fri: 5AM – 11PM",
    sub: "Sat–Sun: 6AM – 10PM"
  }
];

const subjects = [
  "General Inquiry",
  "Membership Plans",
  "Trial Session",
  "Personal Training",
  "Class Schedule",
  "Other"
];

export default function Contact() {
  const [form, setForm] = useState({
    name: "", phone: "", email: "", subject: "", message: ""
  });
  const [status, setStatus] = useState(null);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  const validate = (fields = form) => {
    const e = {};
    if (!fields.name.trim()) e.name = "Name is required";
    if (!fields.phone.trim()) e.phone = "Phone is required";
    else if (!/^\+?[0-9\s\-]{7,}$/.test(fields.phone)) e.phone = "Enter a valid phone number";
    if (fields.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(fields.email)) e.email = "Enter a valid email";
    if (!fields.message.trim() || fields.message.trim().length < 10)
      e.message = "Message must be at least 10 characters";
    return e;
  };

  const handleChange = (field, value) => {
    const updated = { ...form, [field]: value };
    setForm(updated);
    if (touched[field]) {
      setErrors(validate(updated));
    }
  };

  const handleBlur = (field) => {
    setTouched(t => ({ ...t, [field]: true }));
    setErrors(validate());
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    const allTouched = { name: true, phone: true, email: true, message: true };
    setTouched(allTouched);
    const errs = validate();
    setErrors(errs);
    if (Object.keys(errs).length > 0) return;

    setStatus("sending");
    try {
      await contactApi.create({
        name: form.name,
        phone: form.phone,
        email: form.email || null,
        subject: form.subject || "General Inquiry",
        message: form.message
      });
      setStatus("success");
    } catch (err) {
      console.error("Contact submit error:", err);
      setStatus("error");
    }
  };

  const resetForm = () => {
    setForm({ name: "", phone: "", email: "", subject: "", message: "" });
    setStatus(null);
    setErrors({});
    setTouched({});
  };

  return (
    <div className="contact-page">
      <style>{`
        /* ── Matching Website Design System ── */
        :root {
          --contact-accent: #c89f5e;
          --contact-accent-dim: rgba(200,159,94,0.12);
          --contact-accent-glow: rgba(200,159,94,0.2);
          --contact-primary: #1a2028;
          --contact-bg: #f8f9fa;
          --contact-surface: #ffffff;
          --contact-text: #2d3748;
          --contact-text-secondary: #718096;
          --contact-text-muted: #a0aec0;
          --contact-border: #e2e8f0;
          --contact-error: #c53030;
          --contact-success: #2f855a;
        }

        /* ── Hero ── */
        .contact-hero {
          background: linear-gradient(135deg, #1a2028 0%, #2d3748 50%, #1a2028 100%);
          color: #fff;
          padding: 80px 24px 70px;
          text-align: center;
          position: relative;
          overflow: hidden;
        }
        .contact-hero::before {
          content: "";
          position: absolute;
          top: -100px; left: 50%; transform: translateX(-50%);
          width: 600px; height: 600px;
          background: radial-gradient(circle, rgba(200,159,94,0.08) 0%, transparent 70%);
          pointer-events: none;
        }
        .contact-hero-eyebrow {
          display: inline-block;
          font-size: 0.7rem;
          font-weight: 700;
          letter-spacing: 0.2em;
          text-transform: uppercase;
          color: var(--contact-accent);
          background: var(--contact-accent-dim);
          border: 1px solid rgba(200,159,94,0.25);
          padding: 0.35rem 1rem;
          border-radius: 2rem;
          margin-bottom: 1.2rem;
        }
        .contact-hero h1 {
          font-size: clamp(2rem, 5vw, 3.2rem);
          font-weight: 800;
          letter-spacing: -0.03em;
          margin: 0 0 0.75rem;
          line-height: 1.1;
        }
        .contact-hero p {
          opacity: 0.88;
          font-size: 1.05rem;
          max-width: 520px;
          margin: 0 auto;
          line-height: 1.6;
        }

        /* ── Main layout ── */
        .contact-body {
          background: var(--contact-bg);
          padding: 60px 24px 80px;
        }
        .contact-grid {
          max-width: 1080px;
          margin: 0 auto;
          display: grid;
          grid-template-columns: 1fr 1.8fr;
          gap: 32px;
          align-items: start;
        }
        @media (max-width: 900px) {
          .contact-grid {
            grid-template-columns: 1fr;
          }
        }

        /* ── Info panel ── */
        .info-panel {
          background: var(--contact-surface);
          border: 1px solid var(--contact-border);
          border-radius: 12px;
          overflow: hidden;
          position: sticky;
          top: 24px;
          box-shadow: 0 1px 3px rgba(0,0,0,0.04);
        }
        .info-panel-top {
          background: linear-gradient(135deg, #2d3748 0%, #1a2028 100%);
          padding: 28px;
          border-bottom: 1px solid var(--contact-border);
        }
        .info-panel-top h2 {
          font-size: 1.2rem;
          font-weight: 800;
          color: #fff;
          margin: 0 0 0.4rem;
          letter-spacing: -0.01em;
        }
        .info-panel-top p {
          color: rgba(255,255,255,0.7);
          font-size: 0.85rem;
          margin: 0;
          line-height: 1.5;
        }
        .info-items {
          padding: 20px 28px;
          display: flex;
          flex-direction: column;
          gap: 20px;
        }
        .info-item {
          display: grid;
          grid-template-columns: 40px 1fr;
          gap: 12px;
          align-items: start;
        }
        .info-icon {
          width: 40px;
          height: 40px;
          background: var(--contact-accent-dim);
          border: 1px solid rgba(200,159,94,0.2);
          border-radius: 10px;
          display: grid;
          place-items: center;
          font-size: 1.1rem;
          flex-shrink: 0;
        }
        .info-item-text strong {
          display: block;
          font-size: 0.95rem;
          color: var(--contact-text);
          font-weight: 600;
          line-height: 1.3;
        }
        .info-item-text span {
          font-size: 0.82rem;
          color: var(--contact-text-secondary);
          line-height: 1.4;
        }
        .info-divider {
          height: 1px;
          background: var(--contact-border);
          margin: 0 28px;
        }
        .map-link {
          display: flex;
          align-items: center;
          gap: 6px;
          margin: 20px 28px 24px;
          padding: 10px 16px;
          border: 1.5px solid var(--contact-accent);
          border-radius: 8px;
          color: var(--contact-accent);
          font-size: 0.8rem;
          font-weight: 700;
          letter-spacing: 0.05em;
          text-transform: uppercase;
          text-decoration: none;
          transition: all 0.2s;
          justify-content: center;
        }
        .map-link:hover {
          background: var(--contact-accent-dim);
        }
        .map-link svg {
          width: 14px;
          height: 14px;
        }

        /* ── Form card ── */
        .form-card {
          background: var(--contact-surface);
          border: 1px solid var(--contact-border);
          border-radius: 12px;
          padding: 32px;
          box-shadow: 0 1px 3px rgba(0,0,0,0.04);
        }
        @media (max-width: 500px) {
          .form-card { padding: 24px; }
        }
        .form-card h2 {
          font-size: 1.4rem;
          font-weight: 800;
          color: var(--contact-text);
          letter-spacing: -0.02em;
          margin: 0 0 0.3rem;
        }
        .form-card .form-subtitle {
          color: var(--contact-text-secondary);
          font-size: 0.9rem;
          margin: 0 0 28px;
        }
        .form-row {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 16px;
          margin-bottom: 0;
        }
        @media (max-width: 560px) {
          .form-row { grid-template-columns: 1fr; }
        }
        .field {
          display: flex;
          flex-direction: column;
          gap: 6px;
          margin-bottom: 18px;
        }
        .field:last-of-type { margin-bottom: 0; }
        .field label {
          font-size: 0.8rem;
          font-weight: 700;
          letter-spacing: 0.04em;
          text-transform: uppercase;
          color: var(--contact-text-secondary);
        }
        .field label .req {
          color: var(--contact-accent);
          margin-left: 2px;
        }
        .field input,
        .field select,
        .field textarea {
          background: #fff;
          border: 1.5px solid var(--contact-border);
          border-radius: 8px;
          color: var(--contact-text);
          font-size: 0.95rem;
          padding: 10px 14px;
          outline: none;
          transition: border-color 0.2s, box-shadow 0.2s;
          font-family: inherit;
          width: 100%;
          box-sizing: border-box;
        }
        .field input::placeholder,
        .field textarea::placeholder {
          color: var(--contact-text-muted);
        }
        .field input:focus,
        .field select:focus,
        .field textarea:focus {
          border-color: var(--contact-accent);
          box-shadow: 0 0 0 3px var(--contact-accent-dim);
        }
        .field.has-error input,
        .field.has-error select,
        .field.has-error textarea {
          border-color: var(--contact-error);
        }
        .field select {
          appearance: none;
          background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'%3E%3Cpath d='M1 1l5 5 5-5' stroke='%23718096' stroke-width='1.5' fill='none' stroke-linecap='round'/%3E%3C/svg%3E");
          background-repeat: no-repeat;
          background-position: right 14px center;
          padding-right: 40px;
          cursor: pointer;
        }
        .field textarea {
          resize: vertical;
          min-height: 140px;
          line-height: 1.6;
        }
        .field-error {
          font-size: 0.78rem;
          color: var(--contact-error);
          display: flex;
          align-items: center;
          gap: 4px;
        }
        .char-count {
          font-size: 0.75rem;
          color: var(--contact-text-muted);
          text-align: right;
          margin-top: -2px;
        }

        /* ── Submit button ── */
        .submit-btn {
          width: 100%;
          padding: 14px 28px;
          background: var(--contact-primary);
          color: #fff;
          border: none;
          border-radius: 8px;
          font-size: 0.85rem;
          font-weight: 700;
          letter-spacing: 0.08em;
          text-transform: uppercase;
          cursor: pointer;
          transition: background 0.2s, transform 0.15s;
          margin-top: 24px;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 8px;
        }
        .submit-btn:hover:not(:disabled) {
          background: #2d3748;
          transform: translateY(-1px);
        }
        .submit-btn:disabled {
          opacity: 0.7;
          cursor: not-allowed;
          transform: none;
        }
        .submit-btn .spinner {
          width: 16px;
          height: 16px;
          border: 2px solid rgba(255,255,255,0.3);
          border-top-color: #fff;
          border-radius: 50%;
          animation: spin 0.7s linear infinite;
        }
        @keyframes spin { to { transform: rotate(360deg); } }

        /* ── Error banner ── */
        .error-banner {
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 12px 16px;
          background: #fff5f5;
          border: 1px solid #feb2b2;
          border-radius: 8px;
          color: var(--contact-error);
          font-size: 0.9rem;
          margin-top: 20px;
        }

        /* ── Success state ── */
        .success-state {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          text-align: center;
          padding: 48px 32px;
          gap: 16px;
        }
        .success-icon {
          width: 72px;
          height: 72px;
          background: linear-gradient(135deg, #f0fff4, #f0fff4);
          border: 2px solid var(--contact-success);
          border-radius: 50%;
          display: grid;
          place-items: center;
          font-size: 2rem;
          color: var(--contact-success);
          flex-shrink: 0;
        }
        .success-state h3 {
          font-size: 1.5rem;
          font-weight: 800;
          color: var(--contact-text);
          margin: 0;
          letter-spacing: -0.02em;
        }
        .success-state p {
          color: var(--contact-text-secondary);
          font-size: 0.95rem;
          max-width: 380px;
          line-height: 1.6;
          margin: 0;
        }
        .success-tag {
          display: inline-flex;
          align-items: center;
          gap: 6px;
          background: #f0fff4;
          border: 1px solid #9ae6b4;
          border-radius: 2rem;
          padding: 4px 12px;
          font-size: 0.75rem;
          font-weight: 700;
          color: var(--contact-success);
          letter-spacing: 0.05em;
          text-transform: uppercase;
        }
        .success-actions {
          display: flex;
          gap: 12px;
          flex-wrap: wrap;
          justify-content: center;
          margin-top: 8px;
        }
        .btn-ghost {
          padding: 10px 20px;
          border: 1.5px solid var(--contact-border);
          border-radius: 8px;
          background: #fff;
          color: var(--contact-text-secondary);
          font-size: 0.8rem;
          font-weight: 700;
          letter-spacing: 0.04em;
          text-transform: uppercase;
          cursor: pointer;
          transition: all 0.2s;
        }
        .btn-ghost:hover {
          border-color: var(--contact-accent);
          color: var(--contact-accent);
        }
      `}</style>

      {/* Hero */}
      <div className="contact-hero">
        <div className="contact-hero-eyebrow">Get In Touch</div>
        <h1>Talk to IronCore</h1>
        <p>Have a question about memberships, classes, or training? We're here — reach out and we'll get back to you fast.</p>
      </div>

      {/* Body */}
      <div className="contact-body">
        <div className="contact-grid">

          {/* Info panel */}
          <aside className="info-panel">
            <div className="info-panel-top">
              <h2>IronCore Fitness</h2>
              <p>We're open 7 days a week. Walk in or reach us through any channel below.</p>
            </div>
            <div className="info-items">
              {contactDetails.map((item) => (
                <div key={item.label} className="info-item">
                  <div className="info-icon">{item.icon}</div>
                  <div className="info-item-text">
                    <strong>{item.value}</strong>
                    <span>{item.sub}</span>
                  </div>
                </div>
              ))}
            </div>
            <div className="info-divider" />
            <a
              href="https://maps.google.com/?q=Sector+12+Gurugram"
              target="_blank"
              rel="noopener noreferrer"
              className="map-link"
            >
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              Get Directions
            </a>
          </aside>

          {/* Form card */}
          <div className="form-card">
            {status === "success" ? (
              <div className="success-state">
                <div className="success-icon">✓</div>
                <div className="success-tag">
                  <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3">
                    <polyline points="20 6 9 17 4 12"/>
                  </svg>
                  Message Received
                </div>
                <h3>We'll be in touch!</h3>
                <p>Thanks for reaching out. Our team will respond to your message within 24 hours during business hours.</p>
                <div className="success-actions">
                  <button className="btn-ghost" onClick={resetForm}>Send Another Message</button>
                </div>
              </div>
            ) : (
              <>
                <h2>Send a Message</h2>
                <p className="form-subtitle">Fields marked <span style={{ color: "var(--contact-accent)" }}>*</span> are required.</p>

                <form onSubmit={onSubmit} noValidate aria-label="Contact form">
                  <div className="form-row">
                    <div className={`field${errors.name && touched.name ? " has-error" : ""}`}>
                      <label htmlFor="c-name">Your Name <span className="req">*</span></label>
                      <input
                        id="c-name"
                        type="text"
                        placeholder="John Smith"
                        value={form.name}
                        onChange={e => handleChange("name", e.target.value)}
                        onBlur={() => handleBlur("name")}
                        autoComplete="name"
                      />
                      {errors.name && touched.name && (
                        <span className="field-error">⚠ {errors.name}</span>
                      )}
                    </div>
                    <div className={`field${errors.phone && touched.phone ? " has-error" : ""}`}>
                      <label htmlFor="c-phone">Phone <span className="req">*</span></label>
                      <input
                        id="c-phone"
                        type="tel"
                        placeholder="+91 98765 43210"
                        value={form.phone}
                        onChange={e => handleChange("phone", e.target.value)}
                        onBlur={() => handleBlur("phone")}
                        autoComplete="tel"
                      />
                      {errors.phone && touched.phone && (
                        <span className="field-error">⚠ {errors.phone}</span>
                      )}
                    </div>
                  </div>

                  <div className="form-row">
                    <div className={`field${errors.email && touched.email ? " has-error" : ""}`}>
                      <label htmlFor="c-email">Email</label>
                      <input
                        id="c-email"
                        type="email"
                        placeholder="you@example.com"
                        value={form.email}
                        onChange={e => handleChange("email", e.target.value)}
                        onBlur={() => handleBlur("email")}
                        autoComplete="email"
                      />
                      {errors.email && touched.email && (
                        <span className="field-error">⚠ {errors.email}</span>
                      )}
                    </div>
                    <div className="field">
                      <label htmlFor="c-subject">Subject</label>
                      <select
                        id="c-subject"
                        value={form.subject}
                        onChange={e => handleChange("subject", e.target.value)}
                      >
                        <option value="">Select a topic</option>
                        {subjects.map(s => (
                          <option key={s} value={s}>{s}</option>
                        ))}
                      </select>
                    </div>
                  </div>

                  <div className={`field${errors.message && touched.message ? " has-error" : ""}`}>
                    <label htmlFor="c-message">Message <span className="req">*</span></label>
                    <textarea
                      id="c-message"
                      placeholder="Tell us how we can help you..."
                      value={form.message}
                      onChange={e => handleChange("message", e.target.value)}
                      onBlur={() => handleBlur("message")}
                      rows={5}
                    />
                    <div className="char-count">{form.message.length} / 1000</div>
                    {errors.message && touched.message && (
                      <span className="field-error">⚠ {errors.message}</span>
                    )}
                  </div>

                  <button
                    type="submit"
                    className="submit-btn"
                    disabled={status === "sending"}
                  >
                    {status === "sending" ? (
                      <>
                        <span className="spinner" />
                        Sending...
                      </>
                    ) : (
                      <>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M22 2L11 13M22 2l-7 20-4-9-9-4 20-7z"/>
                        </svg>
                        Send Message
                      </>
                    )}
                  </button>

                  {status === "error" && (
                    <div className="error-banner">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                        <circle cx="12" cy="12" r="10"/>
                        <line x1="12" y1="8" x2="12" y2="12"/>
                        <line x1="12" y1="16" x2="12.01" y2="16"/>
                      </svg>
                      Something went wrong. Please try again or call us directly.
                    </div>
                  )}
                </form>
              </>
            )}
          </div>

        </div>
      </div>
    </div>
  );
}