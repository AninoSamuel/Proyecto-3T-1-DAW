
export function crearElementoLogro(logro, mostrarAsignadoA = true, jugadores = []) {
    let jugadorAsignadoNombre = 'N/A';

    if (jugadores && jugadores.length > 0) {
        const jugadorSeleccionado = jugadores[0];
        jugadorAsignadoNombre = `${jugadorSeleccionado.nombre || ''} ${jugadorSeleccionado.apellido || ''}`.trim();
    }
    else if (logro.jugador) {
        jugadorAsignadoNombre = `${logro.jugador.nombre || ''} ${logro.jugador.apellido || ''}`.trim();
    }

    if (jugadorAsignadoNombre === '') {
        jugadorAsignadoNombre = 'N/A';
    }

    let descripcionHTML = '';
    if (jugadorAsignadoNombre !== 'N/A') {
    } else {
        if (logro.descripcion) {
             descripcionHTML = `<p class="card-text text-muted small">${logro.descripcion}</p>`;
        }
    }

    return `
        <div class="card mb-3 w-100 h-100 d-flex flex-column">
            <div class="card-body d-flex flex-column">
                <h5 class="card-title text-primary mb-3">${logro.nombre || 'Logro sin nombre'}</h5>       
                ${descripcionHTML}
            </div>
        </div>
    `;
}