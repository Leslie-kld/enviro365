function formatCurrency(value) {
  return new Intl.NumberFormat('en-ZA', {
    style: 'currency',
    currency: 'ZAR',
    maximumFractionDigits: 2,
  }).format(value ?? 0)
}

export default function PortfolioOverview({ investor, portfolio }) {
  const productCount = portfolio?.investmentProducts?.length ?? 0

  return (
    <div className="portfolio-overview">
      <div className="hero-card card">
        <span className="hero-label">Total portfolio balance</span>
        <span className="hero-value">{formatCurrency(portfolio?.totalBalance)}</span>
        <span className="hero-caption">
          {investor?.firstName} {investor?.lastName} · Portfolio #{portfolio?.id}
        </span>
      </div>

      <div className="stat-grid">
        <div className="stat-card card">
          <span className="stat-label">Investor age</span>
          <span className="stat-value">{investor?.age ?? '—'}</span>
        </div>
        <div className="stat-card card">
          <span className="stat-label">Investment products</span>
          <span className="stat-value">{productCount}</span>
        </div>
        <div className="stat-card card stat-card-gold">
          <span className="stat-label">Maximum withdrawal (90%)</span>
          <span className="stat-value">{formatCurrency(portfolio?.maxWithdrawalAmount)}</span>
        </div>
      </div>

      <style>{`
        .portfolio-overview {
          display: grid;
          grid-template-columns: 1.3fr 1fr;
          gap: 20px;
        }
        .hero-card {
          padding: 28px 32px;
          display: flex;
          flex-direction: column;
          gap: 6px;
          justify-content: center;
          background: linear-gradient(155deg, var(--navy-900) 0%, var(--navy-950) 100%);
          border: none;
        }
        .hero-label {
          color: var(--navy-100);
          font-size: 0.85rem;
        }
        .hero-value {
          font-family: var(--font-display);
          font-size: 2.6rem;
          color: var(--white);
          line-height: 1.1;
        }
        .hero-caption {
          color: var(--gold-400);
          font-size: 0.82rem;
          margin-top: 4px;
        }
        .stat-grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 16px;
        }
        .stat-card {
          padding: 18px 20px;
          display: flex;
          flex-direction: column;
          gap: 6px;
          justify-content: center;
        }
        .stat-card-gold {
          grid-column: span 2;
          background: var(--gold-100);
          border-color: #ecd48a;
        }
        .stat-label {
          font-size: 0.78rem;
          color: var(--ink-soft);
        }
        .stat-value {
          font-family: var(--font-display);
          font-size: 1.5rem;
          color: var(--navy-900);
        }
        @media (max-width: 820px) {
          .portfolio-overview {
            grid-template-columns: 1fr;
          }
        }
      `}</style>
    </div>
  )
}
