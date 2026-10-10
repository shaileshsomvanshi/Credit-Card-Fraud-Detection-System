import { useState } from "react";
import { submitTransaction } from "../api";

const EMPTY = {
  cardNumber: "",
  cardHolderName: "",
  amount: "",
  merchant: "",
  merchantCategory: "GROCERY",
  country: "IN",
  homeCountry: "IN",
};

const CATEGORIES = [
  "GROCERY", "FUEL", "RESTAURANT", "TRAVEL", "ELECTRONICS", "CLOTHING",
  "CRYPTO", "GAMBLING", "WIRE_TRANSFER", "GIFT_CARDS", "ADULT",
];

// One-click demo data (all card numbers pass the Luhn check)
const SAMPLES = {
  "Normal purchase": { cardNumber: "4111111111111111", cardHolderName: "Asha Rao", amount: "850", merchant: "Fresh Mart", merchantCategory: "GROCERY", country: "IN", homeCountry: "IN" },
  "Suspicious foreign": { cardNumber: "5500005555555559", cardHolderName: "Ravi Kumar", amount: "6500", merchant: "CoinVault", merchantCategory: "CRYPTO", country: "RU", homeCountry: "IN" },
};

export default function TransactionForm({ onSubmitted }) {
  const [form, setForm] = useState(EMPTY);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  const update = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setBusy(true);
    setError("");
    setResult(null);
    try {
      const saved = await submitTransaction({ ...form, amount: Number(form.amount) });
      setResult(saved);
      onSubmitted();
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <section className="panel form-panel">
      <h2>Check a transaction</h2>

      <div className="samples">
        <span>Fill with:</span>
        {Object.entries(SAMPLES).map(([name, data]) => (
          <button key={name} type="button" className="link" onClick={() => setForm(data)}>
            {name}
          </button>
        ))}
      </div>

      <form onSubmit={submit}>
        <label>Card number
          <input name="cardNumber" value={form.cardNumber} onChange={update} inputMode="numeric" autoComplete="off" placeholder="4111 1111 1111 1111" required />
        </label>
        <label>Card holder
          <input name="cardHolderName" value={form.cardHolderName} onChange={update} required />
        </label>
        <div className="row">
          <label>Amount
            <input name="amount" type="number" min="0.01" step="0.01" value={form.amount} onChange={update} required />
          </label>
          <label>Merchant category
            <select name="merchantCategory" value={form.merchantCategory} onChange={update}>
              {CATEGORIES.map((c) => <option key={c}>{c}</option>)}
            </select>
          </label>
        </div>
        <label>Merchant
          <input name="merchant" value={form.merchant} onChange={update} required />
        </label>
        <div className="row">
          <label>Purchase country
            <input name="country" value={form.country} onChange={update} maxLength={2} required />
          </label>
          <label>Home country
            <input name="homeCountry" value={form.homeCountry} onChange={update} maxLength={2} required />
          </label>
        </div>
        <button className="primary" disabled={busy}>{busy ? "Scoring..." : "Score transaction"}</button>
      </form>

      {error && <div className="banner error">{error}</div>}

      {result && (
        <div className={"result " + result.status.toLowerCase()}>
          <strong>{result.status} &middot; risk {result.riskScore}/100</strong>
          <p>{result.fraudReasons}</p>
        </div>
      )}
    </section>
  );
}
