export default function Stats() {
  const stats = [
    { value: "500+", label: "Members" },
    { value: "20+", label: "Equipment" },
    { value: "5+", label: "Expert Trainers" },
    { value: "7", label: "Days Open" },
  ];
  return (
    <section className="stats" aria-label="Gym statistics">
      <div className="stats-inner">
        {stats.map((s) => (
          <div className="stat-card" key={s.label}>
            <span className="stat-value">{s.value}</span>
            <span className="stat-label">{s.label}</span>
          </div>
        ))}
      </div>
    </section>
  );
}
