
import { crearBarraNavegacion } from '../components/navbar.js';
import { PartidoAPI } from '../api/partidoAPI.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const rootDiv = document.getElementById('root');
    const urlParams = new URLSearchParams(window.location.search);
    const partidoId = urlParams.get('id');

    if (!partidoId) {
        rootDiv.innerHTML = `
            <div class="container mt-4">
                <p class="alert alert-danger text-center">ID de partido no proporcionado en la URL.</p>
                <a href="listaPartidos.html" class="btn btn-secondary">Volver a la lista de partidos</a>
            </div>
        `;
        return;
    }

    rootDiv.innerHTML = `
        <div class="container mt-4">
            <h2 class="mb-4">Detalle del Partido</h2>
            <div id="partido-detail-card" class="card">
                <div class="card-body">
                    <p class="text-center">Cargando detalles del partido...</p>
                </div>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <a href="listaPartidos.html" class="btn btn-secondary">Volver a la Lista</a>
                <div class="d-flex gap-2">                  <a href="formularioPartido.html?id=${partidoId}" class="btn btn-primary">Editar Partido</a>                  <button id="delete-partido-btn" class="btn btn-danger" data-id="${partidoId}">Eliminar Partido</button>
                </div>
            </div>
        </div>
    `;


    const partidoDetailCardBody = document.querySelector('#partido-detail-card .card-body');

    try {
        const partido = await PartidoAPI.getPartidoById(partidoId);

        const fechaPartido = partido.fecha ? new Date(partido.fecha).toLocaleString('es-ES', { dateStyle: 'full', timeStyle: 'short' }) : 'Fecha desconocida';

        const equipoLocalNombre = partido.equipoLocal ? partido.equipoLocal.nombre : 'N/A';
        const equipoVisitanteNombre = partido.equipoVisitante ? partido.equipoVisitante.nombre : 'N/A';

        partidoDetailCardBody.innerHTML = `
            <h5 class="card-title">${equipoLocalNombre} vs ${equipoVisitanteNombre}</h5>
            <p class="card-text"><strong>Fecha y Hora:</strong> ${fechaPartido}</p>
            <p class="card-text"><strong>Lugar:</strong> ${partido.lugar || 'N/A'}</p>
            <p class="card-text"><strong>Resultado:</strong> ${partido.puntuacionLocal !== undefined ? partido.puntuacionLocal : '?' } - ${partido.puntuacionVisitante !== undefined ? partido.puntuacionVisitante : '?' }</p>
            <p class="card-text">
                <strong>Equipo Local:</strong> 
                ${partido.equipoLocal ? `<a href="detalleEquipo.html?id=${partido.equipoLocal.id}">${partido.equipoLocal.nombre}</a>` : 'N/A'}
            </p>
            <p class="card-text">
                <strong>Equipo Visitante:</strong> 
                ${partido.equipoVisitante ? `<a href="detalleEquipo.html?id=${partido.equipoVisitante.id}">${partido.equipoVisitante.nombre}</a>` : 'N/A'}
            </p>
        `;

        document.getElementById('delete-partido-btn').addEventListener('click', async (event) => {
            const idToDelete = event.target.dataset.id;
            if (confirm(`¿Estás seguro de que quieres eliminar este partido?`)) {
                try {
                    await PartidoAPI.deletePartido(idToDelete);
                    alert('Partido eliminado exitosamente.');
                    window.location.href = 'listaPartidos.html';
                } catch (error) {
                    console.error('Error al eliminar el partido:', error);
                    alert(`Error al eliminar el partido: ${error.message}`);
                }
            }
        });

    } catch (error) {
        console.error('Error al cargar los detalles del partido:', error);
        partidoDetailCardBody.innerHTML = `<p class="alert alert-danger text-center">Error al cargar los detalles del partido: ${error.message}</p>`;
    }
});