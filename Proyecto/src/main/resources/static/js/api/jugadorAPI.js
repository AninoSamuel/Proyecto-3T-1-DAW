import { fetchApi } from '../utils/apiUtils.js';

export const JugadorAPI = {
    getAllJugadores: () => fetchApi('GET', '/v1/jugadores'),
    getJugadorById: (id) => fetchApi('GET', `/v1/jugadores/${id}`),
    createJugador: (jugador) => fetchApi('POST', '/v1/jugadores', jugador),
    updateJugador: (id, jugador) => fetchApi('PUT', `/v1/jugadores/${id}`, jugador),
    deleteJugador: (id) => fetchApi('DELETE', `/v1/jugadores/${id}`),
    searchJugadores: (searchTerm) => fetchApi('GET', `/v1/jugadores/search?nombre=${encodeURIComponent(searchTerm)}`),
    updateJugadorLogros: (jugadorId, logroIds) => fetchApi('PUT', `/v1/jugadores/${jugadorId}/logros`, logroIds)
};