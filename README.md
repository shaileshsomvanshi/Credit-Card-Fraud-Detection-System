# Credit-Card-Fraud-Detection-System
# Fraud Detection System

## Project Overview

The **Fraud Detection System** is a full-stack web application developed to monitor financial transactions, identify potentially fraudulent activities, and help users manage transaction risks efficiently. The application provides an interactive dashboard that displays transaction statistics, processes transactions, and classifies them according to predefined fraud-detection rules.

The main objective of this project is to demonstrate how a modern web application can integrate a frontend, backend, and database to process and monitor financial transactions in a structured and efficient manner.

## Technologies Used

- **Frontend:** React.js, JavaScript, HTML, CSS, Vite
- **Backend:** Java, Spring Boot, Spring Web REST APIs
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **Development Tools:** Eclipse IDE, Visual Studio Code, npm, Git, GitHub

## Key Features

- **Transaction Management:** Submit financial transactions by entering details such as transaction amount, sender account, and receiver account.
- **Fraud Detection:** Evaluate transactions using configurable fraud-score thresholds to identify potentially suspicious activities.
- **Transaction Classification:** Categorize transactions into Approved, Flagged, or Blocked statuses based on the configured business rules.
- **Interactive Dashboard:** Display total transactions, approved transactions, flagged transactions, blocked transactions, blocked transaction amounts, and fraud rate.
- **Transaction History:** Retrieve and view transaction records stored in the database.
- **Filtering:** Filter transactions by their status to make transaction monitoring easier.
- **Transaction Review:** Review flagged transactions and update their decisions through backend API endpoints.
- **REST API Integration:** Connect the React frontend with the Spring Boot backend using HTTP requests.
- **Database Persistence:** Store transaction information in MySQL for retrieval and analysis.

## Application Architecture

The application follows a three-layer architecture:

1. **Presentation Layer:** React.js provides the user interface and dashboard.
2. **Business Logic Layer:** Spring Boot processes transactions, applies fraud-detection rules, and exposes REST APIs.
3. **Data Layer:** MySQL stores transaction records, while JPA/Hibernate supports database operations.

## 🖥️ Application UI & Workflow

The **Card Fraud Monitor** provides real-time fraud scoring and transaction monitoring powered by a dynamic 8-rule evaluation engine. Below is a detailed walkthrough of the interface and scoring lifecycle.
---
### 1. Main Dashboard & Activity Monitoring
The primary control panel summarizes real-time fraud metrics across all processed transactions and displays an audit feed of recent activity.

Dashboard Overview(assets/dashboard-overview.png)

* **Key Metrics Bar:** Tracks total volume, approval count, manual review flags, blocked transactions, total monetary loss prevented, and aggregate suspicious rate.
* **Transaction Feed:** Displays real-time transaction records alongside assigned risk scores (0–100), classification badges (`Approved`, `Needs Review`, `Blocked`), and explicit rationale under the **Why** column for regulatory explainability.
* 
### 2. Status Filtering & Transaction Auditing
The activity table features dedicated tab filters to isolate transactions based on fraud classification.

| Approved Transactions | Blocked Transactions |
| :--- | :--- |
| Approved Filter(assets/filter-approved.png) | Blocked Filter(assets/filter-blocked.png) |
| **Clean Activity:** Displays normal purchases passing all risk checks (e.g., domestic grocery purchases with zero suspicious signals). | **Flagged Activity:** Displays high-risk actions exceeding rule thresholds with full rule violation details. |

---

### 3. Transaction Simulator & Input Form
Allows analysts and developers to manually test transaction payloads against the fraud scoring engine.

Transaction Input Form(assets/transaction-input.png)

* **Quick-Fill Presets:** Pre-populates sample test payloads (e.g., "Normal purchase", "Suspicious foreign").
* **Configurable Attributes:** Captures essential risk evaluation vectors including masked/raw card number, cardholder name, amount, merchant category code (MCC), transaction country, and home country.

---

### 4. Real-Time Risk Scoring & Decision Breakdown
Upon submitting a transaction via **Score transaction**, the rules engine evaluates the payload in real time and provides an actionable decision banner.

Risk Scoring Output]assets/risk-evaluation.png)

* **Risk Score Index:** Quantifies risk from 0 to 100 with clear color-coded severity.
* **Decision Status:** Automatically classifies the transaction into `Approved`, `Needs review`, or `Blocked`.
* **Rule Engine Audit Trail:** Lists the exact trigger conditions that contributed to the score (e.g., high-risk MCC, transaction value threshold $\ge 5000$, and cross-border country mismatches).

## Learning Outcomes

This project demonstrates practical knowledge of full-stack development, Java programming, Spring Boot, REST API development, React components and hooks, database integration, CRUD operations, and frontend-backend communication. It also provides experience in organizing application components and implementing rule-based transaction monitoring.

## Future Enhancements

Future improvements may include machine-learning-based fraud prediction, user authentication and authorization, advanced transaction analytics, real-time alerts, and detailed fraud reports.

## Conclusion

The Fraud Detection System is a practical full-stack development project that combines React.js, Spring Boot, and MySQL to demonstrate transaction management and rule-based fraud monitoring. It can be extended with advanced analytics and intelligent fraud-detection techniques for more comprehensive financial risk management.
