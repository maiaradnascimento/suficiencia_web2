import React, { useState } from 'react';
import { itemService } from '../services/itemService';
import './ItemForm.css';

const ItemForm = ({ onItemCreated, onCancel }) => {
  const [formData, setFormData] = useState({
    nome: '',
    valor: '',
    imagem: null
  });
  const [imagePreview, setImagePreview] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      // Validar tipo de arquivo
      if (!file.type.startsWith('image/')) {
        setError('Por favor, selecione apenas arquivos de imagem.');
        return;
      }

      // Validar tamanho (máximo 5MB)
      if (file.size > 5 * 1024 * 1024) {
        setError('A imagem deve ter no máximo 5MB.');
        return;
      }

      setFormData(prev => ({
        ...prev,
        imagem: file
      }));

      // Criar preview da imagem
      const reader = new FileReader();
      reader.onload = (e) => {
        setImagePreview(e.target.result);
      };
      reader.readAsDataURL(file);
      setError('');
    }
  };

  const removeImage = () => {
    setFormData(prev => ({
      ...prev,
      imagem: null
    }));
    setImagePreview(null);
    document.getElementById('imagem').value = '';
  };

  const validateForm = () => {
    if (!formData.nome.trim()) {
      setError('Nome é obrigatório.');
      return false;
    }

    if (!formData.valor || parseFloat(formData.valor) <= 0) {
      setError('Valor deve ser maior que zero.');
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setLoading(true);
    setError('');

    try {
      let imagemBase64 = null;
      let tipoImagem = null;
      
      // Se há imagem, converter para base64
      if (formData.imagem) {
        const reader = new FileReader();
        imagemBase64 = await new Promise((resolve) => {
          reader.onload = (e) => resolve(e.target.result.split(',')[1]); // Remove data:image/...;base64,
          reader.readAsDataURL(formData.imagem);
        });
        tipoImagem = formData.imagem.type;
      }

      const submitData = {
        nome: formData.nome.trim(),
        valor: parseFloat(formData.valor),
        imagemBase64: imagemBase64,
        tipoImagem: tipoImagem
      };

      const createdItem = await itemService.create(submitData);
      
      // Resetar formulário
      setFormData({
        nome: '',
        valor: '',
        imagem: null
      });
      setImagePreview(null);
      document.getElementById('imagem').value = '';

      if (onItemCreated) {
        onItemCreated(createdItem);
      }
    } catch (err) {
      console.error('Erro ao criar item:', err);
      setError('Erro ao criar item. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="item-form-container">
      <div className="item-form">
        <h2>Cadastrar Novo Item</h2>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="nome">Nome do Item *</label>
            <input
              type="text"
              id="nome"
              name="nome"
              value={formData.nome}
              onChange={handleInputChange}
              placeholder="Digite o nome do item"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="valor">Valor (R$) *</label>
            <input
              type="number"
              id="valor"
              name="valor"
              value={formData.valor}
              onChange={handleInputChange}
              placeholder="0.00"
              min="0.01"
              step="0.01"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="imagem">Imagem do Item</label>
            <input
              type="file"
              id="imagem"
              name="imagem"
              accept="image/*"
              onChange={handleImageChange}
            />
            <small className="file-info">
              Formatos aceitos: JPG, PNG, GIF. Tamanho máximo: 5MB.
            </small>
          </div>

          {imagePreview && (
            <div className="image-preview">
              <img src={imagePreview} alt="Preview" />
              <button type="button" onClick={removeImage} className="remove-image-btn">
                Remover Imagem
              </button>
            </div>
          )}

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          <div className="form-actions">
            <button type="button" onClick={onCancel} className="cancel-btn">
              Cancelar
            </button>
            <button type="submit" disabled={loading} className="submit-btn">
              {loading ? 'Salvando...' : 'Salvar Item'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ItemForm;
