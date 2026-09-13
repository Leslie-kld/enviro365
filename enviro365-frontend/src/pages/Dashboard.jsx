import { useEffect, useState } from 'react'
import { fetchInvestors, fetchInvestorPortfolio } from '../services/api'
import InvestorSelector from '../components/InvestorSelector.jsx'
import PortfolioOverview from '../components/PortfolioOverview.jsx'
import ProductsTable from '../components/ProductsTable.jsx'
import WithdrawalForm from '../components/WithdrawalForm.jsx'
import BusinessRulesCard from '../components/BusinessRulesCard.jsx'
import WithdrawalHistory from '../components/WithdrawalHistory.jsx'

export default function Dashboard() {
  const [investors, setInvestors] = useState([])
  const [selectedInvestorId, setSelectedInvestorId] = useState(null)
  const [portfolioData, setPortfolioData] = useState(null)
  const [loadingInvestors, setLoadingInvestors] = useState(true)
  const [loadingPortfolio, setLoadingPortfolio] = useState(false)
  const [loadError, setLoadError] = useState('')
  const [historyRefreshKey, setHistoryRefreshKey] = useState(0)

  useEffect(() => {
    async function loadInvestors() {
      try {
        const data = await fetchInvestors()
        setInvestors(data)
        if (data.length > 0) setSelectedInvestorId(data[0].id)
      } catch (err) {
        setLoadError(err.message)
      } finally {
        setLoadingInvestors(false)
      }
    }
    loadInvestors()
  }, [])

  useEffect(() => {
    if (!selectedInvestorId) return
    let cancelled = false

    async function loadPortfolio() {
      setLoadingPortfolio(true)
      setLoadError('')
      try {
        const data = await fetchInvestorPortfolio(selectedInvestorId)
        if (!cancelled) setPortfolioData(data)
      } catch (err) {
        if (!cancelled) setLoadError(err.message)
      } finally {
        if (!cancelled) setLoadingPortfolio(false)
      }
    }

    loadPortfolio()
    return () => {
      cancelled = true
    }
  }, [selectedInvestorId])

  function handleWithdrawalSuccess() {
    // Refresh the balance/max-withdrawal figures and the history table
    fetchInvestorPortfolio(selectedInvestorId).then(setPortfolioData).catch(() => {})
    setHistoryRefreshKey((key) => key + 1)
  }

  if (loadError && !portfolioData) {
    return (
      <div className="card" style={{ padding: 24 }}>
        <div className="banner banner-error">{loadError}</div>
        <p style={{ color: 'var(--ink-soft)', fontSize: '0.9rem', marginTop: 12 }}>
          Make sure the Spring Boot backend is running at <code>http://localhost:8080</code>.
        </p>
      </div>
    )
  }

  return (
    <>
      <section>
        <div className="section-heading">
          <div>
            <h2>Welcome back</h2>
            <span className="section-note">Select an investor to view their live portfolio</span>
          </div>
        </div>

        <InvestorSelector
          investors={investors}
          selectedId={selectedInvestorId}
          onChange={setSelectedInvestorId}
          loading={loadingInvestors}
        />
      </section>

      <section>
        <div className="section-heading">
          <h2>Portfolio overview</h2>
        </div>
        {loadingPortfolio || !portfolioData ? (
          <div className="card" style={{ padding: 32, textAlign: 'center', color: 'var(--ink-soft)' }}>
            Loading portfolio…
          </div>
        ) : (
          <>
            <PortfolioOverview investor={portfolioData.investor} portfolio={portfolioData.portfolio} />
            <div style={{ marginTop: 20 }}>
              <ProductsTable products={portfolioData.portfolio.investmentProducts} />
            </div>
          </>
        )}
      </section>

      {portfolioData && (
        <section>
          <div className="section-heading">
            <h2>Withdrawals</h2>
          </div>
          <div className="withdrawal-grid">
            <WithdrawalForm
              investor={portfolioData.investor}
              portfolio={portfolioData.portfolio}
              onWithdrawalSuccess={handleWithdrawalSuccess}
            />
            <BusinessRulesCard />
          </div>
        </section>
      )}

      <section>
        <div className="section-heading">
          <h2>Withdrawal history</h2>
          <span className="section-note">Filter, search, and export withdrawal statements</span>
        </div>
        <WithdrawalHistory selectedInvestorId={selectedInvestorId} refreshKey={historyRefreshKey} />
      </section>

      <style>{`
        .withdrawal-grid {
          display: grid;
          grid-template-columns: 1.3fr 1fr;
          gap: 20px;
          align-items: start;
        }
        @media (max-width: 820px) {
          .withdrawal-grid {
            grid-template-columns: 1fr;
          }
        }
      `}</style>
    </>
  )
}
