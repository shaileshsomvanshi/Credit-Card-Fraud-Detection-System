# Credit Card Fraud Detection System

React (frontend) + Spring Boot / Spring MVC (backend) + MySQL (database)

```
fraud-detection-system/
├── backend/    Spring Boot project (open in Eclipse)
├── frontend/   React + Vite project
└── database/   schema.sql (optional, Hibernate also creates the table)
```

## How it works
A transaction is submitted -> `FraudDetectionService` runs 8 rules and gives a risk score (0-100):

| Rule | Points |
|---|---|
| Amount >= 5000 / >= 2000 | 40 / 20 |
| Foreign country transaction | 20 |
| High-risk merchant (crypto, gambling, wire transfer, gift cards, adult) | 15 |
| Time between 12 AM and 5 AM | 10 |
| Velocity: 3rd / 4th+ use of card within 10 min | 15 / 35 |
| Amount > 5x the card's average | 25 |
| Country changed within 1 hour | 30 |
| First transaction on card is >= 1000 | 10 |

Score >= 70 -> BLOCKED, >= 40 -> FLAGGED (analyst approves/blocks), else APPROVED.
Card numbers are validated with the Luhn algorithm and only a SHA-256 hash + last 4 digits are stored.

## REST API
| Method | URL | Purpose |
|---|---|---|
| POST | /api/transactions | Submit and score a transaction |
| GET | /api/transactions?status=FLAGGED | List (optional filter) |
| GET | /api/transactions/stats | Dashboard numbers |
| PUT | /api/transactions/{id}/review?decision=APPROVED\|BLOCKED | Analyst decision |

## Run in Eclipse
1. Install JDK 17+, MySQL 8, and "Eclipse IDE for Enterprise Java and Web Developers".
2. Make sure MySQL is running. Edit `backend/src/main/resources/application.properties` with your MySQL username/password (the database `fraud_db` is created automatically).
3. Eclipse: **File > Import > Maven > Existing Maven Projects** -> select the `backend` folder -> Finish. Wait for dependencies to download.
4. Right-click the project > **Maven > Update Project** (Alt+F5), and check **Project > Properties > Java Compiler** is 17.
5. Right-click `FraudDetectionApplication.java` > **Run As > Java Application**. Backend runs on http://localhost:8080.

## Run the frontend
Install Node.js 18+ then, in a terminal (Eclipse: Window > Show View > Terminal) inside `frontend/`:
```
npm install
npm run dev
```
Open http://localhost:5173. Use the "Fill with" links to load a normal and a suspicious sample transaction.

## Test cards (pass Luhn)
4111111111111111, 5500005555555559, 378282246310005
