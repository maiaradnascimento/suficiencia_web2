import api from './api';

export const itemService = {
  getAll: async (page = 0, size = 10, sort = 'nome,asc', search = '') => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      sort: sort
    });
    
    if (search && search.trim()) {
      params.append('search', search.trim());
    }
    
    const response = await api.get(`/itens?${params.toString()}`);
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/itens/${id}`);
    return response.data;
  },

  create: async (itemData) => {
    const response = await api.post('/itens', itemData);
    return response.data;
  },

  update: async (id, formData) => {
    const response = await api.put(`/itens/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  delete: async (id) => {
    await api.delete(`/itens/${id}`);
  },

  getImage: async (id) => {
    const response = await api.get(`/itens/${id}/imagem`, {
      responseType: 'blob',
    });
    return response.data;
  },

  search: async (searchTerm, page = 0, size = 10) => {
    const response = await api.get(`/itens`, {
      params: {
        search: searchTerm,
        page,
        size,
        sort: 'nome,asc'
      }
    });
    return response.data;
  }
};
