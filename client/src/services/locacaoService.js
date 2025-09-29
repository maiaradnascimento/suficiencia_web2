import api from './api';

export const locacaoService = {
  getAll: async (page = 0, size = 10, sort = 'data,desc', search = '') => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      sort: sort
    });
    
    if (search && search.trim()) {
      params.append('search', search.trim());
    }
    
    const response = await api.get(`/locacoes?${params.toString()}`);
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/locacoes/${id}`);
    return response.data;
  },

  create: async (locacaoData) => {
    const response = await api.post('/locacoes', locacaoData);
    return response.data;
  },

  update: async (id, locacaoData) => {
    const response = await api.put(`/locacoes/${id}`, locacaoData);
    return response.data;
  },

  delete: async (id) => {
    await api.delete(`/locacoes/${id}`);
  },

  search: async (searchTerm, page = 0, size = 10) => {
    const response = await api.get(`/locacoes/search?q=${searchTerm}&page=${page}&size=${size}`);
    return response.data;
  }
};
