import { useState } from 'react'
import Header from './components/Header.jsx'
import Dashboard from './pages/Dashboard.jsx'

export default function App() {
  const [activeNav, setActiveNav] = useState('Dashboard')

  return (
    <div className="app-shell">
      <Header activeNav={activeNav} onNavChange={setActiveNav} />
      <main className="app-main">
        <Dashboard />
      </main>
    </div>
  )
}
