import React, { useState, useEffect } from 'react';
import { itemService } from '../services/itemService';
import ItemForm from './ItemForm';
import './ItemsList.css';

const ItemsList = () => {
  const [itens, setItens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [pageSize] = useState(12);
  const [searchTerm, setSearchTerm] = useState('');
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    loadItens();
  }, [currentPage, searchTerm]);

  const loadItens = async () => {
    try {
      setLoading(true);
      setError('');
      
      const response = await itemService.getAll(currentPage, pageSize, 'nome,asc', searchTerm);
      setItens(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      setError('Erro ao carregar itens. Tente novamente mais tarde.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(0); // Reset to first page on search
  };

  const handleClearSearch = () => {
    setSearchTerm('');
    setCurrentPage(0);
  };

  const handleItemCreated = (newItem) => {
    setShowForm(false);
    setCurrentPage(0);
    loadItens();
  };

  const handleDeleteItem = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este item?')) {
      try {
        await itemService.delete(id);
        loadItens();
      } catch (err) {
        setError('Erro ao excluir item.');
        console.error(err);
      }
    }
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  };

  const getImageUrl = (item) => {
    if (item.imagemBase64) {
      return `data:${item.tipoImagem || 'image/jpeg'};base64,${item.imagemBase64}`;
    }
    return '/placeholder-image.svg'; // Imagem padrão
  };

  if (showForm) {
    return (
      <ItemForm 
        onItemCreated={handleItemCreated}
        onCancel={() => setShowForm(false)}
      />
    );
  }

  if (loading) {
    return <div className="loading-spinner">Carregando itens...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="items-list-container">
      <div className="items-header">
        <h2>Gerenciar Itens</h2>
        <button onClick={() => setShowForm(true)} className="add-item-btn">
          Adicionar Item
        </button>
      </div>

      <div className="search-section">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Buscar por nome do item..."
            value={searchTerm}
            onChange={handleSearchChange}
          />
          <button onClick={handleClearSearch} className="clear-btn">
            Limpar
          </button>
        </div>
      </div>

      {itens.length === 0 && !loading && (
        <p className="no-data-message">
          {searchTerm ? 'Nenhum item encontrado.' : 'Nenhum item cadastrado.'}
        </p>
      )}

      {itens.length > 0 && (
        <div className="items-grid">
          {itens.map((item) => (
            <div key={item.id} className="item-card">
              <div className="item-image">
                <img 
                  src={getImageUrl(item)} 
                  alt={item.nome}
                  onError={(e) => {
                    e.target.src = '/placeholder-image.svg';
                  }}
                />
              </div>
              <div className="item-info">
                <h3 className="item-name">{item.nome}</h3>
                <p className="item-price">{formatCurrency(item.valor)}</p>
              </div>
              <div className="item-actions">
                <button 
                  onClick={() => handleDeleteItem(item.id)}
                  className="delete-btn"
                >
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {totalPages > 1 && (
        <div className="pagination-controls">
          <button 
            onClick={() => setCurrentPage(0)} 
            disabled={currentPage === 0}
          >
            Primeira
          </button>
          <button 
            onClick={() => setCurrentPage(currentPage - 1)} 
            disabled={currentPage === 0}
          >
            Anterior
          </button>
          <span>
            Página {currentPage + 1} de {totalPages} ({totalElements} itens)
          </span>
          <button 
            onClick={() => setCurrentPage(currentPage + 1)} 
            disabled={currentPage + 1 === totalPages}
          >
            Próxima
          </button>
          <button 
            onClick={() => setCurrentPage(totalPages - 1)} 
            disabled={currentPage + 1 === totalPages}
          >
            Última
          </button>
        </div>
      )}
    </div>
  );
};

export default ItemsList;
