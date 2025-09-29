import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { clienteService } from '../services/clienteService';
import { itemService } from '../services/itemService';
import { locacaoService } from '../services/locacaoService';
import './LocacaoForm.css';

const LocacaoForm = () => {
  const [formData, setFormData] = useState({
    numnota: '',
    data: new Date().toISOString().split('T')[0],
    clienteId: '',
    itens: []
  });
  
  const [clientes, setClientes] = useState([]);
  const [itens, setItens] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [selectedItem, setSelectedItem] = useState({ itemId: '', quantidade: 1, valor: 0 });
  
  const navigate = useNavigate();

  useEffect(() => {
    loadClientes();
    loadItens();
  }, []);

  const loadClientes = async () => {
    try {
      const data = await clienteService.getAll();
      setClientes(data);
    } catch (err) {
      setError('Erro ao carregar clientes');
    }
  };

  const loadItens = async () => {
    try {
      const data = await itemService.getAll(0, 100);
      setItens(data.content || []);
    } catch (err) {
      setError('Erro ao carregar itens');
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleItemSelect = (e) => {
    const itemId = e.target.value;
    const item = itens.find(i => i.id.toString() === itemId);
    setSelectedItem({
      itemId,
      quantidade: 1,
      valor: item ? item.valor : 0
    });
  };

  const handleItemQuantityChange = (e) => {
    const quantidade = parseInt(e.target.value) || 1;
    const item = itens.find(i => i.id.toString() === selectedItem.itemId);
    const valor = item ? item.valor * quantidade : 0;
    
    setSelectedItem(prev => ({
      ...prev,
      quantidade,
      valor
    }));
  };

  const addItem = () => {
    if (!selectedItem.itemId || selectedItem.quantidade <= 0) {
      setError('Selecione um item e quantidade válida');
      return;
    }

    const item = itens.find(i => i.id.toString() === selectedItem.itemId);
    if (!item) return;

    const newItem = {
      itemId: parseInt(selectedItem.itemId),
      quantidade: selectedItem.quantidade,
      valor: selectedItem.valor,
      item: {
        id: item.id,
        nome: item.nome,
        valor: item.valor
      }
    };

    setFormData(prev => ({
      ...prev,
      itens: [...prev.itens, newItem]
    }));

    setSelectedItem({ itemId: '', quantidade: 1, valor: 0 });
    setError('');
  };

  const removeItem = (index) => {
    setFormData(prev => ({
      ...prev,
      itens: prev.itens.filter((_, i) => i !== index)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (!formData.clienteId) {
        throw new Error('Selecione um cliente');
      }

      if (formData.itens.length === 0) {
        throw new Error('Adicione pelo menos um item');
      }

      await locacaoService.create(formData);
      navigate('/locacoes');
    } catch (err) {
      setError(err.message || 'Erro ao criar locação');
    } finally {
      setLoading(false);
    }
  };

  const totalValue = formData.itens.reduce((sum, item) => sum + parseFloat(item.valor), 0);

  return (
    <div className="locacao-form-container">
      <div className="form-header">
        <h2>Nova Locação</h2>
        <button 
          type="button" 
          onClick={() => navigate('/locacoes')}
          className="back-btn"
        >
          ← Voltar
        </button>
      </div>

      <form onSubmit={handleSubmit} className="locacao-form">
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="numnota">Número da Nota:</label>
            <input
              type="text"
              id="numnota"
              name="numnota"
              value={formData.numnota}
              onChange={handleInputChange}
              required
              maxLength={50}
            />
          </div>

          <div className="form-group">
            <label htmlFor="data">Data da Locação:</label>
            <input
              type="date"
              id="data"
              name="data"
              value={formData.data}
              onChange={handleInputChange}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="clienteId">Cliente:</label>
          <select
            id="clienteId"
            name="clienteId"
            value={formData.clienteId}
            onChange={handleInputChange}
            required
          >
            <option value="">Selecione um cliente</option>
            {clientes.map(cliente => (
              <option key={cliente.id} value={cliente.id}>
                {cliente.nome} - {cliente.cpf}
              </option>
            ))}
          </select>
        </div>

        <div className="items-section">
          <h3>Itens da Locação</h3>
          
          <div className="add-item-form">
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="itemSelect">Item:</label>
                <select
                  id="itemSelect"
                  value={selectedItem.itemId}
                  onChange={handleItemSelect}
                >
                  <option value="">Selecione um item</option>
                  {itens.map(item => (
                    <option key={item.id} value={item.id}>
                      {item.nome} - R$ {item.valor.toFixed(2)}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="quantidade">Quantidade:</label>
                <input
                  type="number"
                  id="quantidade"
                  min="1"
                  value={selectedItem.quantidade}
                  onChange={handleItemQuantityChange}
                />
              </div>

              <div className="form-group">
                <label>Valor Total:</label>
                <input
                  type="text"
                  value={`R$ ${selectedItem.valor.toFixed(2)}`}
                  readOnly
                  className="readonly"
                />
              </div>

              <div className="form-group">
                <button type="button" onClick={addItem} className="add-btn">
                  Adicionar Item
                </button>
              </div>
            </div>
          </div>

          {formData.itens.length > 0 && (
            <div className="items-list">
              <h4>Itens Adicionados:</h4>
              <div className="items-table">
                <div className="table-header">
                  <span>Item</span>
                  <span>Quantidade</span>
                  <span>Valor Unit.</span>
                  <span>Valor Total</span>
                  <span>Ações</span>
                </div>
                {formData.itens.map((item, index) => (
                  <div key={index} className="table-row">
                    <span>{item.item.nome}</span>
                    <span>{item.quantidade}</span>
                    <span>R$ {item.item.valor.toFixed(2)}</span>
                    <span>R$ {item.valor.toFixed(2)}</span>
                    <button
                      type="button"
                      onClick={() => removeItem(index)}
                      className="remove-btn"
                    >
                      Remover
                    </button>
                  </div>
                ))}
              </div>
              
              <div className="total-section">
                <strong>Total da Locação: R$ {totalValue.toFixed(2)}</strong>
              </div>
            </div>
          )}
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="form-actions">
          <button type="button" onClick={() => navigate('/locacoes')} className="cancel-btn">
            Cancelar
          </button>
          <button type="submit" disabled={loading} className="submit-btn">
            {loading ? 'Salvando...' : 'Salvar Locação'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default LocacaoForm;
