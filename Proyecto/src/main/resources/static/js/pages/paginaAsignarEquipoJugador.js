
import { crearBarraNavegacion } from '../../components/navbar.js';
import { JugadorAPI } from '../../api/jugadorAPI.js';
import { EquiposAPI } from '../../api/equiposAPI.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    const parametros = new URLSearchParams(window.location.search);
    const idJugador = parametros.get('id');

    if (!idJugador) {
        root.innerHTML = `<p class="text-danger text-center">ID de jugador no especificado en la URL.</p>`;
        return;
    }

    let jugadorActual;
    try {
        jugadorActual = await JugadorAPI.getJugadorById(idJugador); 
        if (!jugadorActual) {
            root.innerHTML = `<p class="text-danger text-center">Jugador con ID ${idJugador} no encontrado.</p>`;
            return;
        }
    } catch (error) {
        console.error('Error al obtener el jugador:', error);
        root.innerHTML = `<p class="text-danger text-center">Error al cargar datos del jugador: ${error.message}</p>`;
        return;
    }

    root.innerHTML = `
    <div class="container mt-4">
        <a href="detalleJugador.html?id=${idJugador}" class="btn btn-link mb-3">← Volver al jugador</a>
        <h2 class="mb-4">Asignar Equipo al Jugador</h2>
        <div class="card p-3 mb-4">
            <p class="mb-1"><strong>${jugadorActual.nombre} ${jugadorActual.apellido}</strong> (Dorsal: ${jugadorActual.dorsal})</p>
            <p class="mb-0">Equipo actual: ${jugadorActual.equipo ?
                `${jugadorActual.equipo.nombre} (ID: ${jugadorActual.equipo.id})` :
                'Sin equipo asignado'}</p>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="mb-3">
                    <label for="equipo-select" class="form-label">Equipo:</label>
                    <select class="form-select" id="equipo-select">
                        <option value="">Seleccione un equipo</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="d-flex gap-2">
            <button id="save-button" class="btn btn-primary" disabled>Guardar Cambios</button>
            ${jugadorActual.equipo ?
                '<button id="unassign-button" class="btn btn-danger">Desasignar Equipo</button>' :
                ''}
        </div>
    </div>`;

    const selectorEquipo = document.getElementById('equipo-select');
    const botonGuardar = document.getElementById('save-button');
    const botonDesasignar = document.getElementById('unassign-button');

    try {
        const equipos = await EquiposAPI.getAllEquipos();
        for (const equipo of equipos) {
            const opcion = document.createElement('option');
            opcion.value = equipo.id;
            opcion.textContent = `${equipo.nombre} (${equipo.ciudad})`;
            selectorEquipo.appendChild(opcion);
        }
        if (jugadorActual.equipo) {
            selectorEquipo.value = jugadorActual.equipo.id;
        }
    } catch (error) {
        console.error('Error al cargar los equipos para el selector:', error);
        alert('No se pudieron cargar los equipos. Por favor, intente de nuevo.');
        return;
    }


    selectorEquipo.addEventListener('change', () => {
        botonGuardar.disabled = !selectorEquipo.value || (jugadorActual.equipo && selectorEquipo.value == jugadorActual.equipo.id);
    });

    botonGuardar.addEventListener('click', async () => {
        if (!selectorEquipo.value) {
            alert('Por favor, seleccione un equipo.');
            return;
        }
        const jugadorActualizado = { ...jugadorActual };
        jugadorActualizado.equipo = { id: selectorEquipo.value }; 
        try {
            await JugadorAPI.updateJugador(idJugador, jugadorActualizado); 
            alert('Equipo asignado exitosamente.');
            window.location.href = `detalleJugador.html?id=${idJugador}`;
        } catch (error) {
            console.error('Error al asignar el equipo:', error);
            alert(`Error al asignar el equipo: ${error.message}`);
        }
    });

    if (botonDesasignar) {
        botonDesasignar.addEventListener('click', async () => {
            const jugadorActualizado = { ...jugadorActual };
            jugadorActualizado.equipo = null; 
            try {
                await JugadorAPI.updateJugador(idJugador, jugadorActualizado);
                alert('Equipo desasignado exitosamente.');
                window.location.href = `detalleJugador.html?id=${idJugador}`;
            } catch (error) {
                console.error('Error al desasignar el equipo:', error);
                alert(`Error al desasignar el equipo: ${error.message}`);
            }
        });
    }
});