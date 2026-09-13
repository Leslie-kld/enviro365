import { useState } from 'react'
import { submitWithdrawal } from '../services/api'

function formatCurrency(value) {
  return new Intl.NumberFormat('en-ZA', {
    style: 'currency',
    currency: 'ZAR',
    maximumFractionDigits: 2,
  }).format(value ?? 0)
}

export default function WithdrawalForm({ investor, portfolio, onWithdrawalSuccess }) {
  const [amount, setAmount] = useState('')
  const [fieldError, setFieldError] = useState('')
  const [serverError, setServerError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const balance = portfolio?.totalBalance ?? 0
  const maxWithdrawal = portfolio?.maxWithdrawalAmount ?? 0

  function validateLocally(value) {
    if (!value) return 'Enter a withdrawal amount.'
    const numeric = Number(value)
    if (Number.isNaN(numeric) || numeric <= 0) return 'Withdrawal amount must be greater than zero.'
    if (numeric > balance) return 'Withdrawal amount cannot exceed the available portfolio balance.'
    if (numeric > maxWithdrawal) return 'Withdrawal amount cannot exceed 90% of the available portfolio balance.'
    return ''
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setServerError('')
    setSuccessMessage('')

    const localError = validateLocally(amount)
    setFieldError(localError)
    if (localError) return

    setSubmitting(true)
    try {
      const response = await submitWithdrawal({
        investorId: investor.id,
        portfolioId: portfolio.id,
        withdrawalAmount: Number(amount),
      })
      setSuccessMessage(
        `Withdrawal of ${formatCurrency(response.withdrawalAmount)} submitted and ${response.status.toLowerCase()}.`
      )
      setAmount('')
      onWithdrawalSuccess?.()
    } catch (err) {
      setServerError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="card withdrawal-form">
      <h3>Submit a withdrawal notice</h3>
      <p className="form-subtitle">
        Available balance <strong>{formatCurrency(balance)}</strong> · Maximum withdrawal{' '}
        <strong>{formatCurrency(maxWithdrawal)}</strong>
      </p>

      <form onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="withdrawal-amount">Withdrawal amount (ZAR)</label>
          <input
            id="withdrawal-amount"
            type="number"
            min="0"
            step="0.01"
            placeholder="0.00"
            value={amount}
            onChange={(e) => {
              setAmount(e.target.value)
              if (fieldError) setFieldError('')
            }}
          />
          {fieldError && <span className="error-text">{fieldError}</span>}
        </div>

        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? 'Submitting…' : 'Submit withdrawal'}
        </button>

        {successMessage && <div className="banner banner-success">{successMessage}</div>}
        {serverError && <div className="banner banner-error">{serverError}</div>}
      </form>

      <style>{`
        .withdrawal-form {
          padding: 24px 26px;
          display: flex;
          flex-direction: column;
          gap: 14px;
        }
        .form-subtitle {
          margin: 0;
          font-size: 0.88rem;
          color: var(--ink-soft);
        }
        .withdrawal-form form {
          display: flex;
          flex-direction: column;
          gap: 14px;
        }
        .withdrawal-form .btn {
          align-self: flex-start;
        }
      `}</style>
    </div>
  )
}
