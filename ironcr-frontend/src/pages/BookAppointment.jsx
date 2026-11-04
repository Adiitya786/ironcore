import { useState } from "react";
import { api } from "../services/api";

export default function BookAppointment() {
  const [form, setForm] = useState({ name: "", phone: "", email: "", preferredDate: "", preferredTime: "", purpose: "", message: "" });
  const [status, setStatus] = useState(null);
  const [errors, setErrors] = useState({});
  const today = new Date().toISOString().split("T")[0];

  const validate = () => {
    const e = {};
    if (!form.name.trim()) e.name = "Name required";
    if (!form.phone.trim()) e.phone = "Phone required";
    else if (!/^\+?[0-9\s-]{7,}$/.test(form.phone)) e.phone = "Invalid phone";
    if (!form.preferredDate) e.preferredDate = "Date required";
    else if (form.preferredDate < today) e.preferredDate = "Date cannot be past";
    if (!form.preferredTime) e.preferredTime = "Time required";
    if (!form.purpose) e.purpose = "Purpose required";
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;
    setStatus("sending");
    const res = await api.createAppointment(form);
    if (res.success) setStatus("success");
    else setStatus("error");
  };

  return (
    <>
      <section className="page-hero"><div className="page-hero-inner"><h1>Book Appointment</h1><p>Schedule a consultation or assessment.</p></div></section>
      <section className="section"><div className="section-inner">
        <form onSubmit={onSubmit} noValidate aria-label="Book appointment" className="form-wide">
          <label htmlFor="a-name">Name *</label><input id="a-name" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
          {errors.name && <span className="error-message">{errors.name}</span>}

          <label htmlFor="a-phone">Phone *</label><input id="a-phone" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} />
          {errors.phone && <span className="error-message">{errors.phone}</span>}

          <label htmlFor="a-email">Email</label><input id="a-email" type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />

          <label htmlFor="a-date">Preferred Date *</label><input id="a-date" type="date" min={today} value={form.preferredDate} onChange={e => setForm({ ...form, preferredDate: e.target.value })} />
          {errors.preferredDate && <span className="error-message">{errors.preferredDate}</span>}

          <label htmlFor="a-time">Preferred Time *</label><input id="a-time" type="time" value={form.preferredTime} onChange={e => setForm({ ...form, preferredTime: e.target.value })} />
          {errors.preferredTime && <span className="error-message">{errors.preferredTime}</span>}

          <label htmlFor="a-purpose">Purpose *</label>
          <select id="a-purpose" value={form.purpose} onChange={e => setForm({ ...form, purpose: e.target.value })}>
            <option value="">Select</option>
            {["Membership Consultation","Personal Training Consultation","Fitness Assessment","General Enquiry"].map(o => <option key={o} value={o}>{o}</option>)}
          </select>
          {errors.purpose && <span className="error-message">{errors.purpose}</span>}

          <label htmlFor="a-msg">Message</label><textarea id="a-msg" rows={3} value={form.message} onChange={e => setForm({ ...form, message: e.target.value })} />

          <button type="submit" className="btn-primary" disabled={status === "sending"}>{status === "sending" ? "SENDING..." : "BOOK APPOINTMENT"}</button>
          {status === "success" && <div className="success-box"><h3>Appointment Request Received</h3><p>We will contact you shortly.</p></div>}
        </form>
      </div></section>
    </>
  );
}
