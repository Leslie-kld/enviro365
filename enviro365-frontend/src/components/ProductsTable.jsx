function formatCurrency(value) {
  return new Intl.NumberFormat('en-ZA', {
    style: 'currency',
    currency: 'ZAR',
    maximumFractionDigits: 2,
  }).format(value ?? 0)
}

function formatProductType(type) {
  return type
    .toLowerCase()
    .split('_')
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ')
}

export default function ProductsTable({ products }) {
  return (
    <div className="card products-table">
      <table>
        <thead>
          <tr>
            <th>Product</th>
            <th>Type</th>
            <th className="align-right">Balance</th>
          </tr>
        </thead>
        <tbody>
          {products.length === 0 && (
            <tr>
              <td colSpan={3} className="empty-cell">
                No investment products found for this portfolio.
              </td>
            </tr>
          )}
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.productName}</td>
              <td>
                <span className="type-chip">{formatProductType(product.productType)}</span>
              </td>
              <td className="align-right">{formatCurrency(product.balance)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <style>{`
        .products-table {
          overflow: hidden;
        }
        .products-table table {
          width: 100%;
          border-collapse: collapse;
        }
        .products-table th {
          text-align: left;
          font-size: 0.76rem;
          text-transform: uppercase;
          letter-spacing: 0.04em;
          color: var(--ink-soft);
          background: var(--paper);
          padding: 12px 20px;
          border-bottom: 1px solid var(--line);
        }
        .products-table td {
          padding: 14px 20px;
          border-bottom: 1px solid var(--line);
          font-size: 0.94rem;
        }
        .products-table tr:last-child td {
          border-bottom: none;
        }
        .align-right {
          text-align: right;
        }
        .type-chip {
          background: var(--navy-100);
          color: var(--navy-800);
          padding: 3px 10px;
          border-radius: 999px;
          font-size: 0.76rem;
        }
        .empty-cell {
          text-align: center;
          color: var(--ink-soft);
          padding: 24px;
        }
      `}</style>
    </div>
  )
}
