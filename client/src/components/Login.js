import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './Login.css';

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    senha: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [isRegister, setIsRegister] = useState(false);
  
  const { login, register } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // Página para redirecionar após login
  const from = location.state?.from?.pathname || '/';

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (isRegister) {
        const registerData = {
          ...formData,
          nome: formData.nome || formData.email.split('@')[0]
        };
        const result = await register(registerData);
        
        if (result.success) {
          setIsRegister(false);
          setFormData({ email: '', senha: '' });
          alert('Usuário registrado com sucesso! Faça login.');
        } else {
          setError(result.error);
        }
      } else {
        const result = await login(formData);
        
        if (result.success) {
          // Redireciona para a página que o usuário tentou acessar ou para home
          navigate(from, { replace: true });
        } else {
          setError(result.error);
        }
      }
    } catch (err) {
      setError('Erro inesperado. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>{isRegister ? 'Registrar' : 'Login'}</h2>
        
        <form onSubmit={handleSubmit}>
          {isRegister && (
            <div className="form-group">
              <label htmlFor="nome">Nome:</label>
              <input
                type="text"
                id="nome"
                name="nome"
                value={formData.nome || ''}
                onChange={handleChange}
                required={isRegister}
              />
            </div>
          )}
          
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="senha">Senha:</label>
            <input
              type="password"
              id="senha"
              name="senha"
              value={formData.senha}
              onChange={handleChange}
              required
              minLength={6}
            />
          </div>
          
          {error && <div className="error-message">{error}</div>}
          
          <button type="submit" disabled={loading} className="submit-btn">
            {loading ? 'Aguarde...' : (isRegister ? 'Registrar' : 'Entrar')}
          </button>
        </form>
        
        <div className="switch-form">
        </div>
      </div>
    </div>
  );
};

export default Login;
