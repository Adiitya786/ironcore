import { useState } from "react";
import { faqs } from "../data/gymData";
import { ChevronDown } from "lucide-react";

const groups = [
  { title: "General", items: faqs.filter(f => ["Do you offer a free trial?","What are your opening hours?","Do you have personal trainers?","Can beginners join?"].includes(f.q)) },
  { title: "Membership", items: faqs.filter(f => ["What membership plans do you offer?"].includes(f.q)) },
  { title: "Training", items: faqs.filter(f => ["Do you provide diet guidance?"].includes(f.q)) },
  { title: "Facilities", items: faqs.filter(f => ["Is parking available?"].includes(f.q)) },
];

export default function FAQ() {
  const [open, setOpen] = useState(null);
  return (
    <>
      <section className="page-hero"><div className="page-hero-inner"><h1>Frequently Asked Questions</h1><p>Quick answers to common questions.</p></div></section>
      <section className="section"><div className="section-inner">
        <div className="faq-categories">
          {groups.map((g) => (
            <div key={g.title}>
              <h2>{g.title}</h2>
              <div className="faq-list">
                {g.items.map((f, i) => (
                  <div className={`faq-item ${open === f.q ? "open" : ""}`} key={f.q}>
                    <button className="faq-question" onClick={() => setOpen(open === f.q ? null : f.q)} aria-expanded={open === f.q} aria-controls={`faq-a-${f.q}`}>
                      <span>{f.q}</span><ChevronDown size={18} aria-hidden="true" />
                    </button>
                    <div id={`faq-a-${f.q}`} className="faq-answer" hidden={open !== f.q}><p>{f.a}</p></div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div></section>
    </>
  );
}
