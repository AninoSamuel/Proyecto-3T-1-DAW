import { crearBarraNavegacion } from '../components/navbar.js';
import { JugadorAPI } from '../api/jugadorAPI.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    console.log('1. DOMContentLoadado. Iniciando carga de página de jugadores.');
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();
    console.log('2. Barra de navegación inyectada.');

    const root = document.getElementById('root');

    const renderJugadoresPage = () => {
        root.innerHTML =             `
            <div class="container mt-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1 class="display-5 mb-0">Gestión de Jugadores</h1>
                    <a href="formularioJugador.html" class="btn btn-success btn-lg shadow-sm">
                        <i class="fas fa-plus-circle me-2"></i>Añadir Nuevo Jugador
                    </a>
                </div>

                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <h5 class="card-title text-primary mb-3"><i class="fas fa-search me-2"></i>Buscar Jugador</h5>
                        <div class="input-group">
                            <input type="text" id="searchInput" class="form-control" placeholder="Buscar jugador por nombre o apellido..." aria-label="Buscar jugador">
                            <button class="btn btn-primary" type="button" id="searchButton">
                                <i class="fas fa-search me-1"></i>Buscar
                            </button>
                            <button class="btn btn-secondary" type="button" id="clearSearchButton">
                                <i class="fas fa-times me-1"></i>Limpiar
                            </button>
                        </div>
                    </div>
                </div>

                <div id="loadingMessage" class="alert alert-info text-center py-3" role="alert" style="display: none;">
                    <div class="spinner-border text-info me-2" role="status">
                        <span class="visually-hidden">Cargando...</span>
                    </div>
                    Cargando Jugadores...
                </div>

                <div class="card shadow-sm">
                    <div class="card-header bg-light">
                        <h5 class="mb-0"><i class="fas fa-users me-2"></i>Lista de Jugadores</h5>
                    </div>
                    <div class="card-body p-0"> <div class="table-responsive">
                            <table class="table table-striped table-hover mb-0" style="display: none;">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre</th>
                                        <th>Apellido</th>
                                        <th>Dorsal</th>
                                        <th>Posición</th>
                                        <th>Equipo</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody id="jugadores-table-body">
                                </tbody>
                            </table>
                        </div>
                        <div id="noPlayersMessage" class="alert alert-warning text-center m-3" role="alert" style="display: none;">
                            <i class="fas fa-exclamation-triangle me-2"></i>No hay jugadores registrados. ¡Añade uno nuevo!
                        </div>
                        <div id="searchNoResultsMessage" class="alert alert-info text-center m-3" role="alert" style="display: none;">
                            <i class="fas fa-info-circle me-2"></i>No se encontraron jugadores con ese criterio de búsqueda.
                        </div>
                        <div id="errorMessage" class="alert alert-danger text-center m-3" role="alert" style="display: none;">
                            <i class="fas fa-exclamation-circle me-2"></i>Ocurrió un error al cargar los jugadores. Por favor, inténtalo de nuevo.
                        </div>
                    </div>
                </div>
            </div>
        `;    };

    renderJugadoresPage();

    const jugadoresTableBody = document.getElementById('jugadores-table-body');
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');
    const clearSearchButton = document.getElementById('clearSearchButton');
    
    const loadingMessage = document.getElementById('loadingMessage');
    const jugadoresTable = document.querySelector('.table');
    const noPlayersMessage = document.getElementById('noPlayersMessage');
    const searchNoResultsMessage = document.getElementById('searchNoResultsMessage');
    const errorMessage = document.getElementById('errorMessage');

    const hideAllMessages = () => {
        loadingMessage.style.display = 'none';
        noPlayersMessage.style.display = 'none';
        searchNoResultsMessage.style.display = 'none';
        errorMessage.style.display = 'none';
        jugadoresTable.style.display = 'none'; 
    };

    async function cargarJugadores(searchTerm = '') {
        hideAllMessages(); 
        loadingMessage.style.display = 'block'; 
        jugadoresTableBody.innerHTML = ''; 

        try {
            let jugadores;
            if (searchTerm) {
                console.log(`Buscando jugadores con término: "${searchTerm}"`);
                jugadores = await JugadorAPI.searchJugadores(searchTerm); 
            } else {
                console.log('Obteniendo todos los jugadores.');
                jugadores = await JugadorAPI.getAllJugadores();
            }

            hideAllMessages(); 

            if (jugadores && jugadores.length > 0) {
                console.log('Jugadores obtenidos, generando filas de tabla.');
                jugadores.forEach(jugador => {
                    const row = document.createElement('tr');
                    
                    row.innerHTML =                         `<td>${jugador.id}</td>
                        <td>${jugador.nombre}</td>
                        <td>${jugador.apellido}</td>
                        <td>${jugador.numeroCamiseta || 'N/A'}</td> 
                        <td>${jugador.posicion || 'N/A'}</td>
                        <td>${jugador.equipo ? jugador.equipo.nombre : 'Sin Equipo'}</td>
                        <td>
                            <a href="detalleJugador.html?id=${jugador.id}" class="btn btn-info btn-sm me-2"><i class="fas fa-eye me-1"></i>Ver Detalles</a>
                            <a href="formularioJugador.html?id=${jugador.id}" class="btn btn-warning btn-sm me-2"><i class="fas fa-edit me-1"></i>Editar</a>
                            <button class="btn btn-danger btn-sm delete-btn" data-id="${jugador.id}"><i class="fas fa-trash-alt me-1"></i>Eliminar</button>
                        </td>`;                    
                    jugadoresTableBody.appendChild(row);
                });
                jugadoresTable.style.display = 'table';
                console.log('Tabla de jugadores renderizada.');

                document.querySelectorAll('.delete-btn').forEach(button => {
                    button.addEventListener('click', async (event) => {
                        const id = event.target.dataset.id;
                        console.log(`Intento de eliminar jugador con ID: ${id}`);
                        if (confirm(`¿Estás seguro de que quieres eliminar al jugador con ID ${id}?`)) {
                            try {
                                await JugadorAPI.deleteJugador(id);
                                alert('Jugador eliminado exitosamente.');
                                console.log('Jugador eliminado, recargando jugadores.');
                                cargarJugadores(searchInput.value); 
                            } catch (error) {
                                console.error('Error al eliminar jugador:', error);
                                alert(`Error al eliminar jugador: ${error.message}`);
                            }
                        }
                    });
                });

            } else {
                if (searchTerm) {
                    searchNoResultsMessage.style.display = 'block'; 
                } else {
                    noPlayersMessage.style.display = 'block';
                }
                jugadoresTable.style.display = 'none'; 
                console.log('No se encontraron jugadores o la lista está vacía.');
            }
        } catch (error) {
            console.error('Error al cargar jugadores:', error);
            errorMessage.textContent = `Ocurrió un error al cargar los jugadores: ${error.message}`;
            errorMessage.style.display = 'block';
            jugadoresTable.style.display = 'none'; 
        }
    }

    searchButton.addEventListener('click', () => {
        const searchTerm = searchInput.value.trim();
        cargarJugadores(searchTerm);
    });

    searchInput.addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault(); 
            const searchTerm = searchInput.value.trim();
            cargarJugadores(searchTerm);
        }
    });

    clearSearchButton.addEventListener('click', () => {
        searchInput.value = ''; 
        cargarJugadores(); 
    });

    cargarJugadores();
});