
import { crearBarraNavegacion } from '../components/navbar.js';
import { JugadorAPI } from '../api/jugadorAPI.js';
import { EquipoAPI } from '../api/equipoAPI.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    const params = new URLSearchParams(window.location.search);
    const idJugador = params.get('id');

    let jugadorActual = {};
    let todosLosEquipos = [];

    try {
        todosLosEquipos = await EquipoAPI.getAllEquipos();
    } catch (error) {
        console.error('Error al cargar los equipos:', error);
        root.innerHTML = `<p class="text-danger text-center">Error al cargar equipos para el selector: ${error.message}</p>`;
        return;
    }

    if (idJugador) {
        try {
            jugadorActual = await JugadorAPI.getJugadorById(idJugador);
            if (!jugadorActual) {
                root.innerHTML = `<p class="text-danger text-center">Jugador con ID ${idJugador} no encontrado.</p>`;
                return;
            }
            document.title = `Editar Jugador: ${jugadorActual.nombre} ${jugadorActual.apellido}`;
        } catch (error) {
            console.error('Error al cargar el jugador para edición:', error);
            root.innerHTML = `<p class="text-danger text-center">Error al cargar datos del jugador: ${error.message}</p>`;
            return;
        }
    } else {
        document.title = 'Crear Nuevo Jugador';
    }

    root.innerHTML = `
    <div class="container mt-4">
        <h2 class="mb-4">${idJugador ? 'Editar Jugador' : 'Crear Nuevo Jugador'}</h2>
        <form id="jugador-form">
            <div class="mb-3">
                <label for="nombre" class="form-label">Nombre:</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${jugadorActual.nombre || ''}" required>
            </div>
            <div class="mb-3">
                <label for="apellido" class="form-label">Apellido:</label>
                <input type="text" class="form-control" id="apellido" name="apellido" value="${jugadorActual.apellido || ''}" required>
            </div>
            <div class="mb-3">
                <label for="numeroCamiseta" class="form-label">Dorsal:</label>
                <input type="number" class="form-control" id="numeroCamiseta" name="numeroCamiseta" value="${jugadorActual.numeroCamiseta || ''}" required min="0" max="99">
            </div>
            <div class="mb-3">
                <label for="posicion" class="form-label">Posición:</label>
                <input type="text" class="form-control" id="posicion" name="posicion" value="${jugadorActual.posicion || ''}">
            </div>
            <div class="mb-3">
                <label for="equipoId" class="form-label">Equipo:</label>
                <select class="form-select" id="equipoId" name="equipoId">
                    <option value="">-- Sin equipo --</option>
                    ${todosLosEquipos.map(equipo => `
                        <option value="${equipo.id}" ${jugadorActual.equipo && jugadorActual.equipo.id == equipo.id ? 'selected' : ''}>
                            ${equipo.nombre} (${equipo.ciudad})
                        </option>
                    `).join('')}
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Guardar Jugador</button>
            <a href="listaJugadores.html" class="btn btn-secondary">Volver a la Lista</a>
        </form>
    </div>`;

    const jugadorForm = document.getElementById('jugador-form');

    jugadorForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new FormData(jugadorForm);
        const equipoId = formData.get('equipoId');

        const jugadorData = {
            nombre: formData.get('nombre'),
            apellido: formData.get('apellido'),
            numeroCamiseta: parseInt(formData.get('numeroCamiseta')),
            posicion: formData.get('posicion'),
            equipo: equipoId ? { id: equipoId } : null,
            logros: jugadorActual.logros || [] 
        };

        try {
            if (idJugador) {
                await JugadorAPI.updateJugador(idJugador, jugadorData);
                alert('Jugador actualizado exitosamente.');
            } else {
                await JugadorAPI.createJugador(jugadorData);
                alert('Jugador creado exitosamente.');
            }
            window.location.href = 'listaJugadores.html';
        } catch (error) {
            console.error('Error al guardar el jugador:', error);
            alert(`Error al guardar el jugador: ${error.message}`);
        }
    });
});