const RULES = [
  'Investor must be older than 65 for a retirement withdrawal',
  'Withdrawal cannot exceed the available portfolio balance',
  'Withdrawal cannot exceed 90% of the portfolio balance',
  'Withdrawal amount must be greater than zero',
]

export default function BusinessRulesCard() {
  return (
    <div className="card rules-card">
      <h3>Withdrawal rules</h3>
      <ul>
        {RULES.map((rule) => (
          <li key={rule}>
            <span className="check" aria-hidden="true">
              <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
                <path d="M2.5 7.5L5.5 10.5L11.5 3.5" stroke="#1e8a5f" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            </span>
            {rule}
          </li>
        ))}
      </ul>

      <style>{`
        .rules-card {
          padding: 24px 26px;
        }
        .rules-card h3 {
          margin-bottom: 14px;
        }
        .rules-card ul {
          list-style: none;
          margin: 0;
          padding: 0;
          display: flex;
          flex-direction: column;
          gap: 12px;
        }
        .rules-card li {
          display: flex;
          align-items: flex-start;
          gap: 10px;
          font-size: 0.9rem;
          color: var(--ink);
        }
        .check {
          flex-shrink: 0;
          width: 22px;
          height: 22px;
          border-radius: 50%;
          background: #e4f3ec;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-top: 1px;
        }
      `}</style>
    </div>
  )
}
