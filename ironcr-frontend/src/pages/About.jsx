import { Link } from "react-router-dom";
import { facilities, gymInfo } from "../data/gymData";

export default function About() {
  return (
    <>
      <section className="page-hero" aria-label="About hero">
        <div className="page-hero-inner">
          <h1>About IronCore Fitness</h1>
          <p>A premium modern gym for people who take fitness seriously.</p>
        </div>
      </section>

      <section className="section" aria-label="Gym story">
        <div className="section-inner two-col">
          <div>
            <h2>Our Story</h2>
            <p>{gymInfo.description}</p>
            <p>Founded with a clear mission — build a workspace where people can train without distractions and get real results. We believe in clean spaces, expert guidance, and consistency.</p>
          </div>
          <img src="https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800&q=80" alt="Gym interior" loading="lazy" />
        </div>
      </section>

      <section className="section section-alt" aria-label="Mission and vision">
        <div className="section-inner">
          <h2>Mission & Vision</h2>
          <div className="cards-grid cards-2">
            <article className="feature-card">
              <h3>Mission</h3>
              <p>Make premium fitness accessible through expert coaching, modern facilities, and a supportive community.</p>
            </article>
            <article className="feature-card">
              <h3>Vision</h3>
              <p>Become the region's most trusted fitness destination — known for results, cleanliness, and real human guidance.</p>
            </article>
          </div>
        </div>
      </section>

      <section className="section" aria-label="Facilities">
        <div className="section-inner">
          <h2>Facilities</h2>
          <div className="cards-grid cards-3">
            {facilities.map((f) => (
              <article className="feature-card" key={f.title}>
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </article>
            ))}
          </div>
        </div>
      </section>

      <section className="section section-alt" aria-label="Gallery">
        <div className="section-inner">
          <h2>Gallery</h2>
          <div className="gallery-grid gallery-4">
            {["https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800&q=80","https://images.unsplash.com/photo-1549068106-b024baf0c729?w=800&q=80","https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&q=80","https://images.unsplash.com/photo-1518611012118-696072aa579a?w=800&q=80"].map((src, i) => (
              <a href={src} key={i} aria-label={`About gallery image ${i + 1}`}><img src={src} alt="Gym view" loading="lazy" /></a>
            ))}
          </div>
        </div>
      </section>

      <section className="cta-banner" aria-label="Book trial call to action">
        <div className="cta-inner">
          <h2>Ready to experience IronCore?</h2>
          <Link to="/book-trial" className="btn-primary btn-lg">BOOK FREE TRIAL</Link>
        </div>
      </section>
    </>
  );
}
