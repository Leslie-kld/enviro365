import { useEffect, useMemo, useState } from 'react'
import { fetchWithdrawals, buildExportUrl } from '../services/api'

function formatCurrency(value) {
  return new Intl.NumberFormat('en-ZA', {
    style: 'currency',
    currency: 'ZAR',
    maximumFractionDigits: 2,
  }).format(value ?? 0)
}

function formatDate(value) {
  return new Date(value).toLocaleString('en-ZA', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

const STATUS_OPTIONS = ['All statuses', 'APPROVED', 'PENDING', 'REJECTED']

export default function WithdrawalHistory({ selectedInvestorId, refreshKey }) {
  const [withdrawals, setWithdrawals] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const [search, setSearch] = useState('')
  const [status, setStatus] = useState('All statuses')
  const [fromDate, setFromDate] = useState('')
  const [toDate, setToDate] = useState('')
  const [scopeToInvestor, setScopeToInvestor] = useState(true)

  useEffect(() => {
    let cancelled = false

    async function load() {
      setLoading(true)
      setError('')
      try {
        const data = await fetchWithdrawals({
          investorId: scopeToInvestor ? selectedInvestorId : undefined,
          status: status === 'All statuses' ? undefined : status,
          fromDate: fromDate || undefined,
          toDate: toDate || undefined,
        })
        if (!cancelled) setWithdrawals(data)
      } catch (err) {
        if (!cancelled) setError(err.message)
      } finally {
        if (!cancelled) setLoading(false)
      }
    }

    load()
    return () => {
      cancelled = true
    }
  }, [selectedInvestorId, status, fromDate, toDate, scopeToInvestor, refreshKey])

  const filtered = useMemo(() => {
    if (!search.trim()) return withdrawals
    const term = search.trim().toLowerCase()
    return withdrawals.filter((w) => w.investorName.toLowerCase().includes(term))
  }, [withdrawals, search])

  const exportUrl = buildExportUrl({
    investorId: scopeToInvestor ? selectedInvestorId : undefined,
    status: status === 'All statuses' ? undefined : status,
    fromDate: fromDate || undefined,
    toDate: toDate || undefined,
  })

  return (
    <div className="card history-card">
      <div className="history-toolbar">
        <div className="field search-field">
          <label htmlFor="history-search">Search by investor</label>
          <input
            id="history-search"
            type="text"
            placeholder="e.g. John Smith"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className="field">
          <label htmlFor="history-status">Status</label>
          <select id="history-status" value={status} onChange={(e) => setStatus(e.target.value)}>
            {STATUS_OPTIONS.map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </select>
        </div>

        <div className="field">
          <label htmlFor="history-from">From date</label>
          <input id="history-from" type="date" value={fromDate} onChange={(e) => setFromDate(e.target.value)} />
        </div>

        <div className="field">
          <label htmlFor="history-to">To date</label>
          <input id="history-to" type="date" value={toDate} onChange={(e) => setToDate(e.target.value)} />
        </div>

        <label className="scope-toggle">
          <input
            type="checkbox"
            checked={scopeToInvestor}
            onChange={(e) => setScopeToInvestor(e.target.checked)}
          />
          Selected investor only
        </label>

        <a className="btn btn-gold export-btn" href={exportUrl} download>
          Download CSV report
        </a>
      </div>

      {error && <div className="banner banner-error history-banner">{error}</div>}

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Investor</th>
            <th className="align-right">Amount</th>
            <th>Date</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {loading && (
            <tr>
              <td colSpan={5} className="state-cell">
                Loading withdrawal history…
              </td>
            </tr>
          )}
          {!loading && filtered.length === 0 && (
            <tr>
              <td colSpan={5} className="state-cell">
                No withdrawals match the current filters.
              </td>
            </tr>
          )}
          {!loading &&
            filtered.map((w) => (
              <tr key={w.id}>
                <td>#{w.id}</td>
                <td>{w.investorName}</td>
                <td className="align-right">{formatCurrency(w.withdrawalAmount)}</td>
                <td>{formatDate(w.withdrawalDate)}</td>
                <td>
                  <span className={`badge badge-${w.status.toLowerCase()}`}>{w.status}</span>
                </td>
              </tr>
            ))}
        </tbody>
      </table>

      <style>{`
        .history-card {
          padding: 22px 26px 8px;
        }
        .history-toolbar {
          display: flex;
          flex-wrap: wrap;
          align-items: flex-end;
          gap: 14px;
          padding-bottom: 18px;
        }
        .search-field {
          min-width: 200px;
          flex: 1;
        }
        .scope-toggle {
          display: flex;
          align-items: center;
          gap: 6px;
          font-size: 0.84rem;
          color: var(--ink-soft);
          padding-bottom: 10px;
          white-space: nowrap;
        }
        .export-btn {
          text-decoration: none;
          white-space: nowrap;
        }
        .history-banner {
          margin-bottom: 14px;
        }
        table {
          width: 100%;
          border-collapse: collapse;
        }
        th {
          text-align: left;
          font-size: 0.76rem;
          text-transform: uppercase;
          letter-spacing: 0.04em;
          color: var(--ink-soft);
          padding: 10px 8px;
          border-bottom: 1px solid var(--line);
        }
        td {
          padding: 13px 8px;
          border-bottom: 1px solid var(--line);
          font-size: 0.9rem;
        }
        .align-right {
          text-align: right;
        }
        .state-cell {
          text-align: center;
          color: var(--ink-soft);
          padding: 30px;
        }
        @media (max-width: 720px) {
          .history-toolbar {
            flex-direction: column;
            align-items: stretch;
          }
        }
      `}</style>
    </div>
  )
}
