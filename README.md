# Enviro365 Investments — Withdrawal System

A full-stack system that lets Enviro365 investors view their portfolios, submit withdrawal notices, and download withdrawal statements — built as a Junior Developer Full-Stack Technical Assessment (eTalente, 2026).

---

## 1. Project Overview

Enviro365 Investments is automating its manual withdrawal notice process. This system allows an investor to:

- View their portfolio (investor details, total balance, investment products)
- Submit a withdrawal notice, validated against four business rules
- View their withdrawal history, with filtering by investor, status and date range
- Export withdrawal statements to CSV

The system is split into two independently runnable projects: a Spring Boot REST API (`enviro365-backend`) and a React + Vite single-page app (`enviro365-frontend`).

## 2. Technologies Used

**Backend:** Java 17, Spring Boot 3.3, Spring Web, Spring Data JPA, Jakarta Bean Validation, H2 (in-memory), Maven, JUnit 5, Mockito.

**Frontend:** React 18, Vite, plain modern CSS (no UI framework) — a custom dark-navy / white / gold financial theme.

## 3. Features

- Portfolio dashboard: investor details, total balance, age, product count, maximum allowed withdrawal, and a table of investment products
- Withdrawal form with live client-side validation (required field, positive amount, balance cap, 90% cap) — backend remains the source of truth
- Withdrawal history table with search by investor name, status filter, date-range filter, loading state and empty state
- One-click CSV download of withdrawal statements, respecting whatever filters are active
- Structured JSON error responses from a global exception handler
- Full DTO layer — JPA entities are never returned directly from a controller

## 4. Architecture

```
Browser (React SPA)
   │  fetch() — JSON over HTTP
   ▼
Spring Boot REST API
   ├── controller/   → HTTP layer, request/response mapping
   ├── service/      → business rules, orchestration
   ├── repository/   → Spring Data JPA, dynamic filtering via Specifications
   ├── entity/       → JPA-mapped domain model
   ├── dto/          → request/response contracts (never expose entities)
   ├── exception/    → custom exceptions + @RestControllerAdvice
   └── config/       → CommandLineRunner data seeding
   ▼
H2 in-memory database
```

**Entity relationships:**

```
Investor 1───1 Portfolio 1───* InvestmentProduct
Investor 1───* WithdrawalNotice *───1 Portfolio
```

## 5. Database Setup

No manual setup required — H2 runs in-memory and is seeded automatically on every backend start via `DataSeeder` (a `CommandLineRunner`). Data resets each time the backend restarts (`spring.jpa.hibernate.ddl-auto=create-drop`).

Seed data: 3 investors (John Smith, 70; Sarah Johnson, 60; Michael Brown, 75), each with a portfolio and 2–3 investment products.

To inspect the data directly, open the H2 console at `http://localhost:8080/h2-console` with:
- JDBC URL: `jdbc:h2:mem:enviro365db`
- User: `sa`
- Password: *(leave blank)*

## 6. Backend Setup

Requirements: JDK 17+, Maven 3.9+ (or use the included wrapper if you add one).

```bash
cd enviro365-backend
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

To run it as a packaged jar instead:

```bash
mvn clean package
java -jar target/enviro365-backend-1.0.0.jar
```

## 7. Frontend Setup

Requirements: Node.js 18+.

```bash
cd enviro365-frontend
npm install
npm run dev
```

The app starts on `http://localhost:5173` and calls the backend at `http://localhost:8080/api` by default. To point at a different backend URL, create a `.env` file in `enviro365-frontend`:

```
VITE_API_BASE_URL=http://localhost:8080/api
```

## 8. API Documentation

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/investors` | List all investors |
| GET | `/api/investors/{id}` | Get one investor |
| GET | `/api/investors/{id}/portfolio` | Get investor details + portfolio + investment products |
| POST | `/api/withdrawals` | Submit a withdrawal notice |
| GET | `/api/withdrawals` | List withdrawals, optionally filtered by `investorId`, `status`, `fromDate`, `toDate` |
| GET | `/api/withdrawals/export` | Download withdrawal history as CSV, same optional filters |

## 9. Example API Requests

**Get an investor's portfolio**
```bash
curl http://localhost:8080/api/investors/1/portfolio
```

**Submit a withdrawal**
```bash
curl -X POST http://localhost:8080/api/withdrawals \
  -H "Content-Type: application/json" \
  -d '{"investorId": 1, "portfolioId": 1, "withdrawalAmount": 5000}'
```

**Filter withdrawal history**
```bash
curl "http://localhost:8080/api/withdrawals?investorId=1&status=APPROVED"
curl "http://localhost:8080/api/withdrawals?fromDate=2026-01-01&toDate=2026-12-31"
```

**Export to CSV**
```bash
curl -OJ "http://localhost:8080/api/withdrawals/export?investorId=1"
```

## 10. Business Rules

All four rules are enforced in `WithdrawalService`, server-side, and mirrored in the frontend form for immediate feedback:

1. **Age restriction** — every product in this system is a retirement investment, so a withdrawal is only allowed if the investor's age is greater than 65.
2. **Balance cap** — the withdrawal amount may not exceed the portfolio's current total balance.
3. **90% cap** — the withdrawal amount may not exceed 90% of the portfolio's current total balance.
4. **Positive amount** — the withdrawal amount must be greater than zero.

A failed rule returns HTTP `400 Bad Request` with a structured body, e.g.:

```json
{
  "timestamp": "2026-09-13T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Withdrawal amount cannot exceed 90% of the available portfolio balance."
}
```

A missing investor/portfolio returns `404 Not Found` with the same shape.

> **Assumption:** the assessment brief describes rule 1 as applying to "retirement withdrawals" without a `withdrawalType` field on the request. Since every investment product seeded and modelled in this system is a retirement product, the age rule is applied to every withdrawal notice.

## 11. CSV Export

`GET /api/withdrawals/export` streams a CSV with the columns: Withdrawal ID, Investor Name, Portfolio ID, Withdrawal Amount, Withdrawal Date, Status. It accepts the same `investorId` / `status` / `fromDate` / `toDate` query parameters as the history endpoint, and sets `Content-Disposition: attachment` so the browser downloads the file directly. The "Download CSV report" button on the frontend links straight to this endpoint with whatever filters are currently active.

## 12. Running Tests

```bash
cd enviro365-backend
mvn test
```

`WithdrawalServiceTest` (JUnit 5 + Mockito) covers:
- A valid withdrawal is approved
- An investor younger than 65 is rejected
- A withdrawal exceeding the portfolio balance is rejected
- A withdrawal exceeding 90% of the balance is rejected
- A zero/negative withdrawal amount is rejected

## 13. Screenshots

*(Add screenshots of the dashboard, withdrawal form, and history table here before submission.)*

## 14. AI Usage Disclosure

AI tools were used to assist with brainstorming, code structure, debugging, and improving documentation. All generated code was reviewed, understood, tested, and modified where necessary.
