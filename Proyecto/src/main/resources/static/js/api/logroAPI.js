
import { fetchApi } from '../utils/apiUtils.js';

export const LogroAPI = {
    getAllLogros: () => fetchApi('GET', '/v1/logros'),
    getLogroById: (id) => fetchApi('GET', `/v1/logros/${id}`),
    createLogro: (logro) => fetchApi('POST', '/v1/logros', logro),
    updateLogro: (id, logro) => fetchApi('PUT', `/v1/logros/${id}`, logro),
    deleteLogro: (id) => fetchApi('DELETE', `/v1/logros/${id}`)
};