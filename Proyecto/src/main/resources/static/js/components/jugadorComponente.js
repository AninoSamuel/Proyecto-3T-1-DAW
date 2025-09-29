
export function crearElementoJugador(jugador) {
    return `
        <a href="detalleJugador.html?id=${jugador.id}" class="card mb-3 text-decoration-none text-dark h-100">
            <div class="card-body">
                <h5 class="card-title">${jugador.nombre} ${jugador.apellido}</h5>
                <p class="card-text text-muted">Posición: ${jugador.posicion || 'N/A'}</p> 
                ${jugador.equipo ? `<p class="card-text"><strong>Equipo:</strong> ${jugador.equipo.nombre} (${jugador.equipo.ciudad})</p>` : 'Sin equipo asignado'}
            </div>
            <div class="card-footer d-flex justify-content-end">
                <a href="formularioJugador.html?id=${jugador.id}" class="btn btn-sm btn-primary me-2">Editar</a>
                <button class="btn btn-sm btn-danger delete-btn" data-id="${jugador.id}">Eliminar</button>
            </div>
        </a>
    `;
}