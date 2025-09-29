import { crearBarraNavegacion } from '../components/navbar.js';
import { JugadorAPI } from '../api/jugadorAPI.js';        
import { LogroAPI } from '../api/logroAPI.js';          

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    const parametros = new URLSearchParams(window.location.search);
    
    const idJugador = parametros.get('jugadorId'); 

    if (!idJugador) {
        root.innerHTML = `
            <div class="container mt-4">
                <p class="alert alert-danger text-center">
                    <i class="fas fa-exclamation-triangle me-2"></i>Error: ID de jugador no especificado en la URL. Asegúrate de acceder desde la página de detalle del jugador.
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
        <h2 class="mb-4">Asignar Logros al Jugador</h2>
        <div class="card p-3 mb-4">
            <p class="mb-0">Logros actuales: ${jugadorActual.logros && jugadorActual.logros.length > 0 ?
                jugadorActual.logros.map(l => l.nombre).join(', ') :
                'Ningún logro asignado'}</p>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="mb-3">
                    <label for="logros-checkbox-list" class="form-label">Seleccionar Logros:</label>
                    <div id="logros-checkbox-list" class="border p-3" style="max-height: 200px; overflow-y: auto;">
                        <p class="text-center">Cargando logros...</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="d-flex gap-2">
            <button id="save-logros-button" class="btn btn-primary" disabled>Guardar Logros</button>
        </div>
    </div>`;

    const logrosCheckboxListDiv = document.getElementById('logros-checkbox-list');
    const botonGuardarLogros = document.getElementById('save-logros-button');

    let todosLosLogros = [];

    try {
        todosLosLogros = await LogroAPI.getAllLogros(); 
        logrosCheckboxListDiv.innerHTML = ''; 

        if (todosLosLogros.length === 0) {
            logrosCheckboxListDiv.innerHTML = '<p class="text-center">No hay logros disponibles.</p>';
            return;
        }

        todosLosLogros.forEach(logro => {
            const divCheck = document.createElement('div');
            divCheck.className = 'form-check';
            divCheck.innerHTML = `
                <input class="form-check-input logro-checkbox" type="checkbox" value="${logro.id}" id="logro-${logro.id}">
                <label class="form-check-label" for="logro-${logro.id}">${logro.nombre}</label>
            `;
            logrosCheckboxListDiv.appendChild(divCheck);

            if (jugadorActual.logros && Array.isArray(jugadorActual.logros) && jugadorActual.logros.some(l => l.id == logro.id)) {
                divCheck.querySelector('.logro-checkbox').checked = true;
            }
        });
        botonGuardarLogros.disabled = false;
    } catch (error) {
        console.error('Error al cargar los logros para el selector:', error);
        logrosCheckboxListDiv.innerHTML = `<p class="text-danger text-center">Error al cargar logros: ${error.message}</p>`;
        return;
    }

    botonGuardarLogros.addEventListener('click', async () => {
        const logrosSeleccionadosIds = Array.from(document.querySelectorAll('.logro-checkbox:checked'))
                                           .map(checkbox => parseInt(checkbox.value)); 

        try {
            await JugadorAPI.updateJugadorLogros(idJugador, logrosSeleccionadosIds); 
            alert('Logros del jugador actualizados exitosamente.');
            window.location.href = `detalleJugador.html?id=${idJugador}`;
        } catch (error) {
            console.error('Error al actualizar los logros del jugador:', error);
            alert(`Error al actualizar logros: ${error.message}`);
        }
    });
});