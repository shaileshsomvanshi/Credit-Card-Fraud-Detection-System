export default function TransactionTable({ rows, onReview }) {
  if (rows.length === 0) {
    return <p className="empty">No transactions yet. Submit one using the form to see it scored here.</p>;
  }

  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Time</th>
            <th>Card</th>
            <th>Merchant</th>
            <th className="num">Amount</th>
            <th>Country</th>
            <th>Risk</th>
            <th>Status</th>
            <th>Why</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {rows.map((t) => (
            <tr key={t.id}>
              <td>{new Date(t.transactionTime).toLocaleString()}</td>
              <td>•••• {t.cardLast4}<br /><small>{t.cardHolderName}</small></td>
              <td>{t.merchant}<br /><small>{t.merchantCategory}</small></td>
              <td className="num">{Number(t.amount).toFixed(2)}</td>
              <td>{t.country}</td>
              <td>
                <div className="meter" title={t.riskScore + "/100"}>
                  <div className={"meter-fill " + t.status.toLowerCase()} style={{ width: t.riskScore + "%" }} />
                </div>
                <small>{t.riskScore}</small>
              </td>
              <td>
                <span className={"pill " + t.status.toLowerCase()}>
                  {t.status === "FLAGGED" ? "Needs review" : t.status.charAt(0) + t.status.slice(1).toLowerCase()}
                </span>
                {t.reviewed && <small className="reviewed">reviewed</small>}
              </td>
              <td className="why">{t.fraudReasons}</td>
              <td className="actions">
                {t.status === "FLAGGED" && (
                  <>
                    <button className="mini ok" onClick={() => onReview(t.id, "APPROVED")}>Approve</button>
                    <button className="mini bad" onClick={() => onReview(t.id, "BLOCKED")}>Block</button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
