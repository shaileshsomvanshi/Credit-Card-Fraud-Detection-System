// All calls to the Spring Boot backend live here.
const BASE = "http://localhost:8080/api/transactions";

async function handle(res) {
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || "Request failed (" + res.status + ")");
  return data;
}

export const submitTransaction = (payload) =>
  fetch(BASE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then(handle);

export const fetchTransactions = (status = "") =>
  fetch(status ? `${BASE}?status=${status}` : BASE).then(handle);

export const fetchStats = () => fetch(`${BASE}/stats`).then(handle);

export const reviewTransaction = (id, decision) =>
  fetch(`${BASE}/${id}/review?decision=${decision}`, { method: "PUT" }).then(handle);
