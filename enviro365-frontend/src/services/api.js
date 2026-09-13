const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

async function handleResponse(response) {
  if (!response.ok) {
    let message = `Request failed with status ${response.status}`
    try {
      const body = await response.json()
      if (body?.message) message = body.message
    } catch {
      // response had no JSON body — fall back to the generic message above
    }
    throw new Error(message)
  }
  return response
}

export async function fetchInvestors() {
  const res = await fetch(`${BASE_URL}/investors`)
  await handleResponse(res)
  return res.json()
}

export async function fetchInvestorPortfolio(investorId) {
  const res = await fetch(`${BASE_URL}/investors/${investorId}/portfolio`)
  await handleResponse(res)
  return res.json()
}

export async function submitWithdrawal({ investorId, portfolioId, withdrawalAmount }) {
  const res = await fetch(`${BASE_URL}/withdrawals`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ investorId, portfolioId, withdrawalAmount }),
  })
  await handleResponse(res)
  return res.json()
}

export async function fetchWithdrawals({ investorId, status, fromDate, toDate } = {}) {
  const params = new URLSearchParams()
  if (investorId) params.set('investorId', investorId)
  if (status) params.set('status', status)
  if (fromDate) params.set('fromDate', fromDate)
  if (toDate) params.set('toDate', toDate)

  const query = params.toString()
  const res = await fetch(`${BASE_URL}/withdrawals${query ? `?${query}` : ''}`)
  await handleResponse(res)
  return res.json()
}

export function buildExportUrl({ investorId, status, fromDate, toDate } = {}) {
  const params = new URLSearchParams()
  if (investorId) params.set('investorId', investorId)
  if (status) params.set('status', status)
  if (fromDate) params.set('fromDate', fromDate)
  if (toDate) params.set('toDate', toDate)

  const query = params.toString()
  return `${BASE_URL}/withdrawals/export${query ? `?${query}` : ''}`
}
