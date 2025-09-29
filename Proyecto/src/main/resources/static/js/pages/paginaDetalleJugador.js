import { crearBarraNavegacion } from '../components/navbar.js';
import { JugadorAPI } from '../api/jugadorAPI.js';
import { LogroAPI } from '../api/logroAPI.js'; 
import { crearElementoLogro } from '../components/logro2Componente.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    console.log('DOM cargado. Iniciando script paginaDetalleJugador.js');

    try {
        document.getElementById('navbar').innerHTML = crearBarraNavegacion();
        console.log('Barra de navegación inyectada.');
    } catch (navError) {
        console.error('Error al inyectar la barra de navegación:', navError);
    }

    const rootDiv = document.getElementById('root');
    const urlParams = new URLSearchParams(window.location.search);
    const jugadorId = urlParams.get('id');

    if (!jugadorId) {
        console.warn('ID de jugador no proporcionado en la URL.');
        rootDiv.innerHTML = `
            <div class="container mt-4">
                <p class="alert alert-danger text-center">
                    <i class="fas fa-exclamation-triangle me-2"></i>Error: ID de jugador no proporcionado en la URL.
                </p>
                <div class="d-grid gap-2 col-6 mx-auto">
                    <a href="listaJugadores.html" class="btn btn-secondary mt-3">
                        <i class="fas fa-arrow-left me-2"></i>Volver a la lista de jugadores
                    </a>
                </div>
            </div>
        `;
        return; 
    }
    console.log(`ID de jugador detectado: ${jugadorId}`);

    rootDiv.innerHTML = `
        <div class="container mt-4">
            <h2 class="mb-4 text-center">Detalle del Jugador</h2>
            <div id="jugador-detail-card" class="card shadow-sm">
                <div class="card-body">
                    <p class="text-center text-muted">Cargando detalles del jugador...</p>
                </div>
            </div>

            <h3 class="mt-5 mb-3 text-center">Logros del Jugador</h3>
            <div class="d-flex justify-content-end mb-3">
                <a href="asignarLogroAJugador.html?jugadorId=${jugadorId}" class="btn btn-success">
                    <i class="fas fa-trophy me-2"></i>Asignar Logro Existente
                </a>
            </div>
            <div id="logros-list" class="row g-3">
                <p class="text-center text-muted">Cargando logros...</p>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <a href="listaJugadores.html" class="btn btn-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Volver a la Lista
                </a>
                <div>
                    <a href="formularioJugador.html?id=${jugadorId}" class="btn btn-primary me-2">
                        <i class="fas fa-edit me-2"></i>Editar Jugador
                    </a>
                    <button id="delete-jugador-btn" class="btn btn-danger" data-id="${jugadorId}">
                        <i class="fas fa-trash-alt me-2"></i>Eliminar Jugador
                    </button>
                </div>
            </div>
        </div>
    `;

    const jugadorDetailCardBody = document.querySelector('#jugador-detail-card .card-body');
    const logrosListDiv = document.getElementById('logros-list');
    const deleteJugadorBtn = document.getElementById('delete-jugador-btn');

    try {
        console.log(`Intentando obtener detalles del jugador con ID: ${jugadorId}`);
        const jugador = await JugadorAPI.getJugadorById(jugadorId);
        console.log('Datos del jugador recibidos:', jugador); //

        if (!jugador || Object.keys(jugador).length === 0) {
            console.warn(`Jugador con ID ${jugadorId} no encontrado o datos vacíos.`);
            jugadorDetailCardBody.innerHTML = `
                <p class="alert alert-warning text-center">
                    <i class="fas fa-exclamation-circle me-2"></i>No se encontraron detalles para el jugador con ID ${jugadorId}.
                </p>
            `;
            logrosListDiv.innerHTML = '';
            return;
        }

        const fechaNacimiento = jugador.fechaNacimiento
            ? new Date(jugador.fechaNacimiento).toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' })
            : 'N/A';

        const esActivo = typeof jugador.activo === 'boolean' ? jugador.activo : false;
        const estadoActivo = esActivo ? 'Sí' : 'No';
        const activoIcon = esActivo ? '<i class="fas fa-check-circle text-success me-2"></i>' : '<i class="fas fa-times-circle text-danger me-2"></i>';

        jugadorDetailCardBody.innerHTML = `
            <h5 class="card-title text-primary mb-3">
                <i class="fas fa-user-circle me-2"></i>${jugador.nombre || 'N/A'} ${jugador.apellido || 'N/A'}
            </h5>
            <div class="row">
                <div class="col-md-6">
                    <p class="card-text mb-1"><strong><i class="fas fa-crosshairs me-2"></i>Posición:</strong> ${jugador.posicion || 'N/A'}</p>
                    <p class="card-text mb-1"><strong><i class="fas fa-sort-numeric-up-alt me-2"></i>Nº Camiseta:</strong> ${jugador.numeroCamiseta || 'N/A'}</p>
                    <p class="card-text mb-1"><strong><i class="fas fa-ruler-vertical me-2"></i>Altura:</strong> ${jugador.alturaCm ? `${jugador.alturaCm} cm` : 'N/A'}</p>
                    <p class="card-text mb-1"><strong><i class="fas fa-weight-hanging me-2"></i>Peso:</strong> ${jugador.pesoKg ? `${jugador.pesoKg} kg` : 'N/A'}</p>
                </div>
                <div class="col-md-6">
                    <p class="card-text mb-1"><strong><i class="fas fa-birthday-cake me-2"></i>Fecha Nacimiento:</strong> ${fechaNacimiento}</p>
                    <p class="card-text mb-1"><strong><i class="fas fa-hourglass-start me-2"></i>Edad:</strong> ${jugador.edad || 'N/A'}</p>
                    <p class="card-text mb-1"><strong>${activoIcon}Activo:</strong> ${estadoActivo}</p>
                    <p class="card-text mb-1">
                        <strong><i class="fas fa-shield-alt me-2"></i>Equipo:</strong>
                        ${jugador.equipo ? `<a href="detalleEquipo.html?id=${jugador.equipo.id}" class="text-decoration-none text-info fw-bold">${jugador.equipo.nombre} (${jugador.equipo.ciudad})</a>` : 'Sin equipo asignado'}
                    </p>
                </div>
            </div>
        `;
        console.log('Detalles del jugador renderizados.');

        const logros = Array.isArray(jugador.logros) ? jugador.logros : []; 
        console.log('Logros del jugador obtenidos (desde jugador.logros):', logros); 

        logrosListDiv.innerHTML = ''; 

        if (logros.length === 0) {
            logrosListDiv.innerHTML = '<p class="text-center text-muted">No hay logros asignados a este jugador.</p>';
        } else {
            logros.forEach(logro => {
                const colDiv = document.createElement('div');
                colDiv.className = 'col-12 col-sm-6 col-md-6 col-lg-4 d-flex align-items-stretch';
                
                colDiv.innerHTML = crearElementoLogro(logro, false, [jugador]);
                logrosListDiv.appendChild(colDiv);
            });
            console.log('Logros del jugador renderizados.');
        }

        if (deleteJugadorBtn) {
            deleteJugadorBtn.addEventListener('click', async () => {
                const jugadorNombreCompleto = `${jugador.nombre || ''} ${jugador.apellido || ''}`.trim();
                if (confirm(`¿Estás seguro de que quieres eliminar a ${jugadorNombreCompleto || 'este jugador'}? Esta acción es irreversible.`)) {
                    try {
                        await JugadorAPI.deleteJugador(jugadorId);
                        alert('Jugador eliminado exitosamente.');
                        window.location.href = 'listaJugadores.html';
                    } catch (error) {
                        console.error('Error al eliminar el jugador:', error);
                        alert(`Error al eliminar el jugador: ${error.message || 'Ocurrió un error desconocido.'}`);
                    }
                }
            });
            console.log('Event listener para eliminar jugador configurado.');
        } else {
            console.warn('Botón de eliminar jugador no encontrado en el DOM.');
        }

    } catch (error) {
        console.error('Error general al cargar la página de detalle del jugador:', error);
        jugadorDetailCardBody.innerHTML = `
            <p class="alert alert-danger text-center">
                <i class="fas fa-exclamation-triangle me-2"></i>Error al cargar los detalles del jugador: ${error.message || 'Ocurrió un error desconocido.'}
            </p>
        `;
        logrosListDiv.innerHTML = `<p class="text-danger text-center"><i class="fas fa-exclamation-triangle me-2"></i>Error al cargar los logros: ${error.message || 'Ocurrió un error desconocido.'}</p>`;
    }
    console.log('Fin del script paginaDetalleJugador.js');
});