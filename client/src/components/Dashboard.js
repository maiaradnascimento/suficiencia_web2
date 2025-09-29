import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import LocacoesList from './LocacoesList';
import ItemsList from './ItemsList';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [activeTab, setActiveTab] = useState('home');

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Sistema de Locadora</h1>
        <div className="user-info">
          <span>Bem-vindo, {user?.displayName}!</span>
          <button onClick={logout} className="logout-btn">
            Sair
          </button>
        </div>
      </header>
      
      <nav className="dashboard-nav">
        <button 
          onClick={() => setActiveTab('home')}
          className={`nav-btn ${activeTab === 'home' ? 'active' : ''}`}
        >
          Dashboard
        </button>
        <button 
          onClick={() => setActiveTab('locacoes')}
          className={`nav-btn ${activeTab === 'locacoes' ? 'active' : ''}`}
        >
          Locações
        </button>
        <button 
          onClick={() => setActiveTab('clientes')}
          className={`nav-btn ${activeTab === 'clientes' ? 'active' : ''}`}
        >
          Clientes
        </button>
        <button 
          onClick={() => setActiveTab('itens')}
          className={`nav-btn ${activeTab === 'itens' ? 'active' : ''}`}
        >
          Itens
        </button>
      </nav>

      <main className="dashboard-content">
        {activeTab === 'home' && (
          <div className="welcome-card">
            <h2>Dashboard</h2>
            <p>Você está logado como: <strong>{user?.username}</strong></p>
            <p>Perfil: <strong>{user?.authorities?.map(auth => auth.authority).join(', ')}</strong></p>
            
            <div className="features-grid">
              <div className="feature-card" onClick={() => setActiveTab('clientes')}>
                <h3>Clientes</h3>
                <p>Gerenciar clientes da locadora</p>
              </div>
              
              <div className="feature-card" onClick={() => setActiveTab('itens')}>
                <h3>Itens</h3>
                <p>Gerenciar itens para locação</p>
              </div>
              
              <div className="feature-card" onClick={() => setActiveTab('locacoes')}>
                <h3>Locações</h3>
                <p>Gerenciar locações ativas</p>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'locacoes' && <LocacoesList />}
        
        {activeTab === 'clientes' && (
          <div className="coming-soon">
            <h2>Clientes</h2>
            <p>Funcionalidade em desenvolvimento...</p>
          </div>
        )}
        
               {activeTab === 'itens' && <ItemsList />}
      </main>
    </div>
  );
};

export default Dashboard;
