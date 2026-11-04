import { testimonials } from "../../data/gymData";
import { Star } from "lucide-react";

export default function Testimonials() {
  return (
    <section className="section" aria-label="Testimonials">
      <div className="section-inner">
        <h2 className="section-heading">What Members Say</h2>
        <div className="testimonials-row">
          {testimonials.map((t, i) => (
            <article className="testimonial-card" key={i}>
              <div className="testimonial-stars" aria-label={`${t.stars} stars`}>
                {Array.from({ length: t.stars }).map((_, j) => (
                  <Star key={j} size={16} fill="#c89f5e" stroke="#c89f5e" aria-hidden="true" />
                ))}
              </div>
              <blockquote className="testimonial-text">"{t.review}"</blockquote>
              <p className="testimonial-author">— {t.name}</p>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
