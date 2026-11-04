import { useState } from "react";
import { faqs } from "../../data/gymData";
import { ChevronDown } from "lucide-react";

export default function FAQPreview() {
  const [open, setOpen] = useState(0);
  return (
    <section className="section section-alt" aria-label="Frequently asked questions">
      <div className="section-inner">
        <h2 className="section-heading">Common Questions</h2>
        <div className="faq-list">
          {faqs.map((f, i) => (
            <div className={`faq-item ${open === i ? "open" : ""}`} key={i}>
              <button
                className="faq-question"
                onClick={() => setOpen(open === i ? -1 : i)}
                aria-expanded={open === i}
                aria-controls={`faq-answer-${i}`}
              >
                <span>{f.q}</span>
                <ChevronDown size={18} aria-hidden="true" />
              </button>
              <div id={`faq-answer-${i}`} className="faq-answer" hidden={open !== i}>
                <p>{f.a}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
