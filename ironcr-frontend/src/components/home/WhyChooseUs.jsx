import { Dumbbell, Award, Clock, Sparkles, Target, Layers } from "lucide-react";

const icons = { Dumbbell, Award, Clock, Sparkles, Target, Layers };

const cards = [
  { title: "Modern Equipment", desc: "State-of-the-art machines maintained daily for peak performance.", iconKey: "Dumbbell" },
  { title: "Expert Trainers", desc: "Certified professionals with years of real-world experience.", iconKey: "Award" },
  { title: "Flexible Timings", desc: "Early mornings to late nights — train on your schedule.", iconKey: "Clock" },
  { title: "Clean Facility", desc: "Hygiene-first approach with frequent deep cleaning.", iconKey: "Sparkles" },
  { title: "Personal Guidance", desc: "Tailored plans based on your body, goals, and lifestyle.", iconKey: "Target" },
  { title: "Multiple Programs", desc: "From strength to weight loss — something for every goal.", iconKey: "Layers" },
];

export default function WhyChooseUs() {
  return (
    <section className="section" aria-label="Why choose us">
      <div className="section-inner">
        <h2 className="section-heading">Why IronCore?</h2>
        <div className="cards-grid cards-3">
          {cards.map((c) => {
            const Icon = icons[c.iconKey] || Dumbbell;
            return (
              <article className="feature-card" key={c.title}>
                <div className="feature-icon" aria-hidden="true"><Icon size={28} strokeWidth={1.5} /></div>
                <h3>{c.title}</h3>
                <p>{c.desc}</p>
              </article>
            );
          })}
        </div>
      </div>
    </section>
  );
}
