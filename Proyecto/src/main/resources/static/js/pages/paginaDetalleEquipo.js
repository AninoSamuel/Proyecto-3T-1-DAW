
import { crearBarraNavegacion } from '../components/navbar.js';
import { EquipoAPI } from '../api/equipoAPI.js'; 
import { crearElementoJugador } from '../components/jugadorComponente.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const rootDiv = document.getElementById('root');
    const urlParams = new URLSearchParams(window.location.search);
    const equipoId = urlParams.get('id');

    if (!equipoId) {
        rootDiv.innerHTML = `
            <div class="container mt-4">
                <p class="alert alert-danger text-center">ID de equipo no proporcionado en la URL.</p>
                <a href="listaEquipos.html" class="btn btn-secondary">Volver a la lista de equipos</a>
            </div>
        `;
        return;
    }

    rootDiv.innerHTML = `
        <div class="container mt-4">
            <h2 class="mb-4">Detalle del Equipo</h2>
            <div id="equipo-detail-card" class="card">
                <div class="card-body">
                    <p class="text-center">Cargando detalles del equipo...</p>
                </div>
            </div>

            
            <div class="d-flex justify-content-between mt-4">
                <a href="listaEquipos.html" class="btn btn-secondary">Volver a la Lista</a>
                <div>
                    <a href="formularioEquipo.html?id=${equipoId}" class="btn btn-primary me-2">Editar Equipo</a>
                </div>
            </div>
        </div>
    `;

    const equipoDetailCardBody = document.querySelector('#equipo-detail-card .card-body');
    const jugadoresListDiv = document.getElementById('jugadores-list');

    try {

        const equipo = await EquipoAPI.getEquipoById(equipoId);

        equipoDetailCardBody.innerHTML = `
        <h5 class="card-title">${equipo.nombre}</h5>
        <p class="card-text"><strong>Ciudad:</strong> ${equipo.ciudad || 'N/A'}</p>
        <p class="card-text"><strong>Abreviatura:</strong> ${equipo.abreviatura || 'N/A'}</p>
        <p class="card-text"><strong>Pabellón:</strong> ${equipo.pabellon || 'N/A'}</p>
        <p class="card-text"><strong>Entrenador:</strong> ${equipo.entrenador || 'N/A'}</p>
        <p class="card-text"><strong>Año:</strong> ${equipo.anioFundacion || 'N/A'}</p>
    `;


    } catch (error) {
        console.error('Error al cargar los detalles del equipo:', error);
        equipoDetailCardBody.innerHTML = `<p class="alert alert-danger text-center">Error al cargar los detalles del equipo: ${error.message}</p>`;
        jugadoresListDiv.innerHTML = `<p class="text-danger text-center">Error al cargar los jugadores: ${error.message}</p>`;
    }
});