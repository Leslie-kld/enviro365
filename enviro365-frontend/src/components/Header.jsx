const NAV_ITEMS = ['Dashboard', 'Withdrawals', 'Reports']

export default function Header({ activeNav, onNavChange }) {
  return (
    <header className="app-header">
      <div className="app-header-inner">
        <div className="brand">
          <span className="brand-mark" aria-hidden="true">
            <svg width="22" height="22" viewBox="0 0 32 32" fill="none">
              <path d="M6 20 L13 10 L18 17 L26 6" stroke="#e2bd4a" strokeWidth="2.6" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </span>
          <div className="brand-text">
            <span className="brand-name">Enviro365</span>
            <span className="brand-sub">Investments</span>
          </div>
        </div>

        <nav className="app-nav">
          {NAV_ITEMS.map((item) => (
            <button
              key={item}
              className={`nav-link ${activeNav === item ? 'nav-link-active' : ''}`}
              onClick={() => onNavChange(item)}
            >
              {item}
            </button>
          ))}
        </nav>
      </div>

      <style>{`
        .app-header {
          background: var(--navy-900);
          border-bottom: 3px solid var(--gold-500);
        }
        .app-header-inner {
          max-width: 1180px;
          margin: 0 auto;
          padding: 18px 24px;
          display: flex;
          align-items: center;
          justify-content: space-between;
        }
        .brand {
          display: flex;
          align-items: center;
          gap: 10px;
        }
        .brand-mark {
          width: 34px;
          height: 34px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: var(--navy-950);
          border-radius: 8px;
        }
        .brand-text {
          display: flex;
          flex-direction: column;
          line-height: 1.1;
        }
        .brand-name {
          font-family: var(--font-display);
          color: var(--white);
          font-size: 1.15rem;
        }
        .brand-sub {
          font-size: 0.72rem;
          color: var(--navy-100);
          letter-spacing: 0.04em;
        }
        .app-nav {
          display: flex;
          gap: 4px;
        }
        .nav-link {
          background: transparent;
          border: none;
          color: var(--navy-100);
          font-size: 0.9rem;
          padding: 8px 14px;
          border-radius: 6px;
          cursor: pointer;
        }
        .nav-link:hover {
          background: rgba(255, 255, 255, 0.08);
          color: var(--white);
        }
        .nav-link-active {
          background: rgba(226, 189, 74, 0.15);
          color: var(--gold-400);
        }
        @media (max-width: 640px) {
          .app-header-inner {
            flex-direction: column;
            align-items: flex-start;
            gap: 12px;
          }
        }
      `}</style>
    </header>
  )
}
