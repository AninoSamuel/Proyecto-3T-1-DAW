import { fetchApi } from '../utils/apiUtils.js';

export const PartidoAPI = { 
    getAllPartidos: () => fetchApi('GET', '/v1/partidos'),
    getPartidoById: (id) => fetchApi('GET', `/v1/partidos/${id}`),
    createPartido: (partido) => fetchApi('POST', '/v1/partidos', partido),
    updatePartido: (id, partido) => fetchApi('PUT', `/v1/partidos/${id}`, partido),
    deletePartido: (id) => fetchApi('DELETE', `/v1/partidos/${id}`)
};