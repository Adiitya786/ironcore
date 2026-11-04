import { useState } from "react";
import { trialsApi } from "../services/api";

export default function BookTrial() {
  const [form, setForm] = useState({ fullName: "", phone: "", email: "", preferredDate: "", preferredTime: "", fitnessGoal: "", experience: "", message: "" });
  const [status, setStatus] = useState(null);
  const [errors, setErrors] = useState({});

  const today = new Date().toISOString().split("T")[0];

  const validate = () => {
    const e = {};
    if (!form.fullName.trim()) e.fullName = "Full name required";
    if (!form.phone.trim()) e.phone = "Phone required";
    else if (!/^\+?[0-9\s-]{7,}$/.test(form.phone)) e.phone = "Invalid phone";
    if (!form.preferredDate) e.preferredDate = "Date required";
    else if (form.preferredDate < today) e.preferredDate = "Date cannot be in the past";
    if (!form.preferredTime) e.preferredTime = "Time required";
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;
    setStatus("sending");
    try {
      await trialsApi.create({
        fullName: form.fullName,
        phone: form.phone,
        email: form.email,
        preferredDate: form.preferredDate,
        preferredTime: form.preferredTime,
        fitnessGoal: form.fitnessGoal,
        experience: form.experience,
        message: form.message
      });
      setStatus("success");
    } catch (err) {
      console.error("Trial booking error:", err);
      setStatus("error");
    }
  };

  return (
    <>
      <section className="page-hero"><div className="page-hero-inner"><h1>Book Your Free Trial</h1><p>Come try IronCore before you commit.</p></div></section>
      <section className="section"><div className="section-inner">
        <form onSubmit={onSubmit} noValidate aria-label="Book trial form" className="form-wide">
          <label htmlFor="t-name">Full Name *</label>
          <input id="t-name" value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} />
          {errors.fullName && <span className="error-message">{errors.fullName}</span>}

          <label htmlFor="t-phone">Phone Number *</label>
          <input id="t-phone" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} />
          {errors.phone && <span className="error-message">{errors.phone}</span>}

          <label htmlFor="t-email">Email</label>
          <input id="t-email" type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />

          <label htmlFor="t-date">Preferred Date *</label>
          <input id="t-date" type="date" value={form.preferredDate} min={today} onChange={e => setForm({ ...form, preferredDate: e.target.value })} />
          {errors.preferredDate && <span className="error-message">{errors.preferredDate}</span>}

          <label htmlFor="t-time">Preferred Time *</label>
          <input id="t-time" type="time" value={form.preferredTime} onChange={e => setForm({ ...form, preferredTime: e.target.value })} />
          {errors.preferredTime && <span className="error-message">{errors.preferredTime}</span>}

          <label htmlFor="t-goal">Fitness Goal</label>
          <select id="t-goal" value={form.fitnessGoal} onChange={e => setForm({ ...form, fitnessGoal: e.target.value })}>
            <option value="">Select</option>
            {["Weight Loss","Muscle Gain","Strength","General Fitness","Bodybuilding","Other"].map(o => <option key={o} value={o}>{o}</option>)}
          </select>

          <label htmlFor="t-exp">Experience Level</label>
          <select id="t-exp" value={form.experience} onChange={e => setForm({ ...form, experience: e.target.value })}>
            <option value="">Select</option>
            {["Beginner","Intermediate","Advanced"].map(o => <option key={o} value={o}>{o}</option>)}
          </select>

          <label htmlFor="t-msg">Message</label>
          <textarea id="t-msg" rows={3} value={form.message} onChange={e => setForm({ ...form, message: e.target.value })} />

          <button type="submit" className="btn-primary" disabled={status === "sending"}>{status === "sending" ? "SENDING..." : "BOOK TRIAL"}</button>
          {status === "success" && <div className="success-box"><h3>Booking Request Received</h3><p>Thank you! Our team will contact you shortly.</p></div>}
          {status === "error" && <p className="error-msg">Something went wrong. Please try again.</p>}
        </form>
      </div></section>
    </>
  );
}
