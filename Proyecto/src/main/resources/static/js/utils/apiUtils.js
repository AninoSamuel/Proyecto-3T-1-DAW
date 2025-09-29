import { API_CONFIG } from '../config/apiConfig.js';

export async function fetchApi(method = 'GET', path, body = null) {
    let url = `${API_CONFIG.baseURL}${path}`;
    let response = {};

    if (method === 'POST' || method === 'PUT' || method === 'PATCH') {
        console.log('Original body in fetchApi:', body); 
        const stringifiedBody = JSON.stringify(body);
        console.log('Stringified body in fetchApi:', stringifiedBody); 

        response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: stringifiedBody
        });

    } else {
        response = await fetch(url, { method: method });
    }

    if (response.status === 204) {
        return null;
    }

    if (!response.ok) {
        const errorText = await response.text();
        console.error("Respuesta de error completa del servidor:", errorText); 
        let errorMessage = `Error desconocido (${response.statusText})`;
        try {
            const errorJson = JSON.parse(errorText);
            errorMessage = errorJson.message || errorText;
        } catch (e) {
            errorMessage = errorText;
        }
        throw new Error(`Error en la API: ${response.status} - ${errorMessage}`);
    }

    try {
        return await response.json();
    } catch (jsonError) {
        console.error("Error al parsear JSON de la respuesta:", jsonError);
        throw new Error(`Error en la API: La respuesta no es JSON válido o está vacía. ${response.status} - ${response.statusText}`);
    }
}