import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { locacaoService } from '../services/locacaoService';
import './LocacoesList.css';

const LocacoesList = () => {
  const [locacoes, setLocacoes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [pageSize] = useState(10);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadLocacoes();
  }, [currentPage]);

  const loadLocacoes = async () => {
    try {
      setLoading(true);
      setError('');
      
      let response;
      if (searchTerm.trim()) {
        response = await locacaoService.search(searchTerm, currentPage, pageSize);
      } else {
        response = await locacaoService.getAll(currentPage, pageSize);
      }

      if (response.content) {
        setLocacoes(response.content);
        setTotalPages(response.totalPages);
        setTotalElements(response.totalElements);
      } else {
        setLocacoes(response || []);
        setTotalPages(1);
        setTotalElements(response?.length || 0);
      }
    } catch (err) {
      setError('Erro ao carregar locações: ' + (err.response?.data?.message || err.message));
      setLocacoes([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0);
    loadLocacoes();
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };

  const calculateTotal = (locacao) => {
    if (!locacao.itens || locacao.itens.length === 0) {
      return 0;
    }
    return locacao.itens.reduce((total, item) => {
      return total + (item.quantidade * item.valor);
    }, 0);
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('pt-BR');
  };

  if (loading) {
    return (
      <div className="locacoes-container">
        <div className="loading">
          <div className="loading-spinner"></div>
          <p>Carregando locações...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="locacoes-container">
      <div className="locacoes-header">
        <h2>Locações</h2>
        
        <div className="header-actions">
          <Link to="/locacoes/nova" className="new-locacao-btn">
            + Nova Locação
          </Link>
        </div>
        
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="Buscar por cliente ou número da nota..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
          <button type="submit" className="search-btn">
            Buscar
          </button>
          {searchTerm && (
            <button 
              type="button" 
              onClick={() => {
                setSearchTerm('');
                setCurrentPage(0);
                loadLocacoes();
              }}
              className="clear-btn"
            >
              Limpar
            </button>
          )}
        </form>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="locacoes-info">
        <p>Total de {totalElements} locações encontradas</p>
      </div>

      {locacoes.length === 0 ? (
        <div className="no-data">
          <p>Nenhuma locação encontrada.</p>
        </div>
      ) : (
        <>
          <div className="locacoes-table">
            <table>
              <thead>
                <tr>
                  <th>Número da Nota</th>
                  <th>Cliente</th>
                  <th>Data</th>
                  <th>Valor Total</th>
                  <th>Itens</th>
                </tr>
              </thead>
              <tbody>
                {locacoes.map((locacao) => (
                  <tr key={locacao.id}>
                    <td>{locacao.numnota}</td>
                    <td>{locacao.cliente?.nome || 'N/A'}</td>
                    <td>{formatDate(locacao.data)}</td>
                    <td className="currency">{formatCurrency(calculateTotal(locacao))}</td>
                    <td>{locacao.itens?.length || 0} item(s)</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="pagination">
            <button 
              onClick={() => handlePageChange(0)}
              disabled={currentPage === 0}
              className="pagination-btn"
            >
              Primeira
            </button>
            
            <button 
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 0}
              className="pagination-btn"
            >
              Anterior
            </button>
            
            <span className="pagination-info">
              Página {currentPage + 1} de {totalPages}
            </span>
            
            <button 
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage >= totalPages - 1}
              className="pagination-btn"
            >
              Próxima
            </button>
            
            <button 
              onClick={() => handlePageChange(totalPages - 1)}
              disabled={currentPage >= totalPages - 1}
              className="pagination-btn"
            >
              Última
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default LocacoesList;
