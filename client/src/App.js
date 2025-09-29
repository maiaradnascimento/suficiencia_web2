import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import Login from './components/Login';
import Home from './pages/Home';
import ItemsList from './components/ItemsList';
import LocacoesList from './components/LocacoesList';
import LocacaoForm from './components/LocacaoForm';
import './App.css';

function AppContent() {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">Carregando...</div>
      </div>
    );
  }

  return (
    <div className="App">
      <Routes>
        <Route 
          path="/login" 
          element={
            isAuthenticated() ? <Navigate to="/dashboard" replace /> : <Login />
          } 
        />
        
        <Route path="/" element={
          isAuthenticated() ? <Navigate to="/dashboard" replace /> : <Navigate to="/login" replace />
        } />
        
        <Route path="/dashboard" element={
          <ProtectedRoute>
            <Layout>
              <Home />
            </Layout>
          </ProtectedRoute>
        } />
        
        <Route path="/itens" element={
          <ProtectedRoute>
            <Layout>
              <ItemsList />
            </Layout>
          </ProtectedRoute>
        } />
        
        <Route path="/locacoes" element={
          <ProtectedRoute>
            <Layout>
              <LocacoesList />
            </Layout>
          </ProtectedRoute>
        } />
        
        <Route path="/locacoes/nova" element={
          <ProtectedRoute>
            <Layout>
              <LocacaoForm />
            </Layout>
          </ProtectedRoute>
        } />
        
        {/* Redireciona qualquer rota desconhecida */}
        <Route path="*" element={
          isAuthenticated() ? <Navigate to="/dashboard" replace /> : <Navigate to="/login" replace />
        } />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

export default App;

