
export function crearElementoLogro(logro, jugadores = [], mostrarAsignadoA = true) {
    let jugadoresHtml = '';
    
    const jugadoresAsociados = logro.jugadores; 

    if (mostrarAsignadoA && jugadoresAsociados && jugadoresAsociados.length > 0) {
        jugadoresHtml = `
            <div class="mt-auto border-top pt-2">
                <p class="card-text small mb-1"><strong>Ganado por:</strong></p>
                <ul class="list-unstyled mb-0 small">
        `;
        jugadoresAsociados.forEach(jugador => {
            if (jugador.nombre && jugador.apellido) {
                jugadoresHtml += `<li>${jugador.nombre} ${jugador.apellido}</li>`;
            } else {
                jugadoresHtml += `<li>Jugador desconocido</li>`;
            }
        });
        jugadoresHtml += `
                </ul>
            </div>
        `;
    } else if (mostrarAsignadoA) {
        jugadoresHtml = `
            <div class="mt-auto border-top pt-2">
                <p class="card-text small mb-0"><strong>Ganado por:</strong> N/A</p>
            </div>
        `;
    }

    return `
        <div class="card mb-3 w-100 h-100 d-flex flex-column">
            <div class="card-body d-flex flex-column">
                <h5 class="card-title text-primary mb-3">${logro.nombre || 'Logro sin nombre'}</h5>

                ${jugadoresHtml}

                <div class="d-flex justify-content-end mt-3">
                    <a href="formularioLogro.html?id=${logro.id}" class="btn btn-sm btn-primary me-2">
                        <i class="fas fa-edit"></i> Editar
                    </a>
                    <button class="btn btn-sm btn-danger delete-btn" data-id="${logro.id}">
                        <i class="fas fa-trash-alt"></i> Eliminar
                    </button>
                </div>
            </div>
        </div>
    `;
}