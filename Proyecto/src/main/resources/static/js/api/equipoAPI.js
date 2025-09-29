
import { fetchApi } from '../utils/apiUtils.js';

export const EquipoAPI = {
    getAllEquipos: () => fetchApi('GET', '/v1/equipos'),
    getEquipoById: (id) => fetchApi('GET', `/v1/equipos/${id}`),
    createEquipo: (equipo) => fetchApi('POST', '/v1/equipos', equipo),
    deleteEquipo: (id) => fetchApi('DELETE', `/v1/equipos/${id}`),
    patchEquipo: (id, equipo) => fetchApi('PATCH', `/v1/equipos/${id}`, equipo)
}; 