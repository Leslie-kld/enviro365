export default function InvestorSelector({ investors, selectedId, onChange, loading }) {
  return (
    <div className="investor-selector card">
      <div className="field">
        <label htmlFor="investor-select">Viewing portfolio for</label>
        <select
          id="investor-select"
          value={selectedId ?? ''}
          onChange={(e) => onChange(Number(e.target.value))}
          disabled={loading || investors.length === 0}
        >
          {investors.map((investor) => (
            <option key={investor.id} value={investor.id}>
              {investor.firstName} {investor.lastName} — Age {investor.age}
            </option>
          ))}
        </select>
      </div>

      <style>{`
        .investor-selector {
          padding: 16px 20px;
          max-width: 360px;
        }
        .investor-selector select {
          min-width: 260px;
        }
      `}</style>
    </div>
  )
}
