
import { crearBarraNavegacion } from '../components/navbar.js';
import { LogrosAPI } from '../api/logrosAPI.js';
import { JugadoresAPI } from '../api/jugadoresAPI.js'; 
import { crearElementoJugador } from '../components/jugadorComponente.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const rootDiv = document.getElementById('root');
    const urlParams = new URLSearchParams(window.location.search);
    const logroId = urlParams.get('id');

    if (!logroId) {
        rootDiv.innerHTML = `
            <div class="container mt-4">
                <p class="alert alert-danger text-center">ID de logro no proporcionado en la URL.</p>
                <a href="listaLogros.html" class="btn btn-secondary">Volver a la lista de logros</a>
            </div>
        `;
        return;
    }

    rootDiv.innerHTML = `
        <div class="container mt-4">
            <h2 class="mb-4">Detalle del Logro</h2>
            <div id="logro-detail-card" class="card">
                <div class="card-body">
                    <p class="text-center">Cargando detalles del logro...</p>
                </div>
            </div>
            
            <h3 class="mt-5 mb-3">Jugadores con este Logro</h3>
            <div id="jugadores-con-logro-list" class="row">
                <p class="text-center">Cargando jugadores...</p>
            </div>
            
            <div class="d-flex justify-content-between mt-4">
                <a href="listaLogros.html" class="btn btn-secondary">Volver a la Lista</a>
                <div>
                    <a href="formularioLogro.html?id=${logroId}" class="btn btn-primary me-2">Editar Logro</a>
                    <button id="delete-logro-btn" class="btn btn-danger" data-id="${logroId}">Eliminar Logro</button>
                </div>
            </div>
        </div>
    `;

    const logroDetailCardBody = document.querySelector('#logro-detail-card .card-body');
    const jugadoresConLogroListDiv = document.getElementById('jugadores-con-logro-list');

    try {
        const logro = await LogrosAPI.getLogroById(logroId);

        logroDetailCardBody.innerHTML = `
            <h5 class="card-title">${logro.nombre}</h5>
            <p class="card-text"><strong>Descripción:</strong> ${logro.descripcion || 'N/A'}</p>
        `;

        const jugadoresAsociados = logro.jugadores || await JugadoresAPI.getAllJugadores().then(all => all.filter(j => j.logros && j.logros.some(l => l.id == logroId))); // Esto es una simplificación, lo ideal sería un endpoint dedicado.


        jugadoresConLogroListDiv.innerHTML = ''; 

        if (jugadoresAsociados.length === 0) {
            jugadoresConLogroListDiv.innerHTML = '<p class="text-center">Ningún jugador tiene este logro asignado.</p>';
        } else {
            jugadoresAsociados.forEach(jugador => {
                const colDiv = document.createElement('div');
                colDiv.className = 'col-md-6 col-lg-4';
                colDiv.innerHTML = crearElementoJugador(jugador);
                jugadoresConLogroListDiv.appendChild(colDiv);
            });
        }
        
        document.getElementById('delete-logro-btn').addEventListener('click', async (event) => {
            const idToDelete = event.target.dataset.id;
            if (confirm(`¿Estás seguro de que quieres eliminar el logro "${logro.nombre}"?`)) {
                try {
                    await LogrosAPI.deleteLogro(idToDelete);
                    alert('Logro eliminado exitosamente.');
                    window.location.href = 'listaLogros.html';
                } catch (error) {
                    console.error('Error al eliminar el logro:', error);
                    alert(`Error al eliminar el logro: ${error.message}`);
                }
            }
        });

    } catch (error) {
        console.error('Error al cargar los detalles del logro:', error);
        logroDetailCardBody.innerHTML = `<p class="alert alert-danger text-center">Error al cargar los detalles del logro: ${error.message}</p>`;
        jugadoresConLogroListDiv.innerHTML = `<p class="text-danger text-center">Error al cargar los jugadores asociados: ${error.message}</p>`;
    }
});