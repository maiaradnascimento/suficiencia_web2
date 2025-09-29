import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './Home.css';

const Home = () => {
  const { user } = useAuth();

  return (
    <div className="home-container">
      <div className="welcome-card">
        <h2>Bem-vindo ao Sistema de Locadora</h2>
        <p>Olá, <strong>{user?.displayName || user?.username}</strong>!</p>

        <div className="features-grid">
          <div className="feature-card">
            <h3>📦 Itens</h3>
            <p>Gerencie os itens disponíveis para locação</p>
            <Link to="/itens" className="feature-link">Acessar</Link>
          </div>

          <div className="feature-card">
            <h3>📋 Locações</h3>
            <p>Visualize e gerencie as locações ativas</p>
            <Link to="/locacoes" className="feature-link">Acessar</Link>
          </div>

          <div className="feature-card">
            <h3>➕ Nova Locação</h3>
            <p>Cadastre uma nova locação</p>
            <Link to="/locacoes/nova" className="feature-link">Criar</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
