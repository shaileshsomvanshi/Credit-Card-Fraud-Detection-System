import { useCallback, useEffect, useState } from "react";
import { fetchStats, fetchTransactions, reviewTransaction } from "./api";
import StatsBar from "./components/StatsBar.jsx";
import TransactionForm from "./components/TransactionForm.jsx";
import TransactionTable from "./components/TransactionTable.jsx";

const FILTERS = [
  { label: "All", value: "" },
  { label: "Approved", value: "APPROVED" },
  { label: "Needs review", value: "FLAGGED" },
  { label: "Blocked", value: "BLOCKED" },
];

export default function App() {
  const [transactions, setTransactions] = useState([]);
  const [stats, setStats] = useState(null);
  const [filter, setFilter] = useState("");
  const [error, setError] = useState("");

  const load = useCallback(async () => {
    try {
      const [list, s] = await Promise.all([fetchTransactions(filter), fetchStats()]);
      setTransactions(list);
      setStats(s);
      setError("");
    } catch {
      setError("Cannot reach the backend. Start the Spring Boot app on port 8080 and check MySQL.");
    }
  }, [filter]);

  useEffect(() => {
    load();
  }, [load]);

  const handleReview = async (id, decision) => {
    try {
      await reviewTransaction(id, decision);
      load();
    } catch (e) {
      setError(e.message);
    }
  };

  return (
    <div className="page">
      <header className="top">
        <h1>Card Fraud Monitor</h1>
        <p>Every transaction is scored against 8 risk rules the moment it is submitted.</p>
      </header>

      {error && <div className="banner error">{error}</div>}

      <StatsBar stats={stats} />

      <div className="layout">
        <TransactionForm onSubmitted={load} />

        <section className="panel">
          <div className="panel-head">
            <h2>Transactions</h2>
            <div className="tabs" role="tablist">
              {FILTERS.map((f) => (
                <button
                  key={f.value}
                  className={filter === f.value ? "tab active" : "tab"}
                  onClick={() => setFilter(f.value)}
                >
                  {f.label}
                </button>
              ))}
            </div>
          </div>
          <TransactionTable rows={transactions} onReview={handleReview} />
        </section>
      </div>
    </div>
  );
}
