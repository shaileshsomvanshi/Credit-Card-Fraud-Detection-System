export default function StatsBar({ stats }) {
  const s = stats || { total: 0, approved: 0, flagged: 0, blocked: 0, blockedAmount: 0, fraudRate: 0 };
  const items = [
    { label: "Transactions", value: s.total, cls: "" },
    { label: "Approved", value: s.approved, cls: "ok" },
    { label: "Needs review", value: s.flagged, cls: "warn" },
    { label: "Blocked", value: s.blocked, cls: "bad" },
    { label: "Amount blocked", value: Number(s.blockedAmount).toLocaleString(undefined, { maximumFractionDigits: 2 }), cls: "bad" },
    { label: "Suspicious rate", value: s.fraudRate + "%", cls: "" },
  ];
  return (
    <div className="stats">
      {items.map((i) => (
        <div key={i.label} className={"stat " + i.cls}>
          <span className="stat-value">{i.value}</span>
          <span className="stat-label">{i.label}</span>
        </div>
      ))}
    </div>
  );
}
