import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './Layout.css';

const Layout = ({ children }) => {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  return (
    <div className="layout">
      <header className="navbar">
        <div className="navbar-container">
          <Link to="/" className="navbar-brand">
            🏪 Sistema Locadora
          </Link>
          
          <nav className="navbar-nav">
            <Link to="/dashboard" className={`nav-link ${isActive('/dashboard')}`}>
              🏠 Dashboard
            </Link>
            <Link to="/itens" className={`nav-link ${isActive('/itens')}`}>
              📦 Itens
            </Link>
            <Link to="/locacoes" className={`nav-link ${isActive('/locacoes')}`}>
              📋 Locações
            </Link>
          </nav>

          <div className="navbar-user">
            <span className="user-info">
              Olá, {user?.displayName || user?.username}
            </span>
            <button onClick={handleLogout} className="logout-btn">
              🚪 Sair
            </button>
          </div>
        </div>
      </header>

      <main className="main-content">
        {children}
      </main>

      <footer className="footer">
        <div className="footer-container">
          <p>&copy; 2024 Sistema de Locadora. Todos os direitos reservados.</p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;
