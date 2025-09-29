
export function crearElementoPartido(partido) {
    const fechaPartido = partido.fecha ? new Date(partido.fecha).toLocaleString('es-ES', { dateStyle: 'medium', timeStyle: 'short' }) : 'Fecha desconocida';


    const equipoLocalNombre = partido.equipoLocal ? partido.equipoLocal.nombre : 'Equipo Local Desconocido';
    const equipoVisitanteNombre = partido.equipoVisitante ? partido.equipoVisitante.nombre : 'Equipo Visitante Desconocido';


    return `
        <a href="detallePartido.html?id=${partido.id}" class="card mb-3 text-decoration-none text-dark">
            <div class="card-body">
                <h5 class="card-title">Partido ${equipoLocalNombre} vs ${equipoVisitanteNombre}</h5>
                <p class="card-text text-muted"><strong>Fecha:</strong> ${fechaPartido}</p>
                <div class="mt-2 border-top pt-2">
                    <p class="card-text small mb-0"><strong>Resultado:</strong> ${partido.puntuacionLocal !== undefined ? partido.puntuacionLocal : '?' } - ${partido.puntuacionVisitante !== undefined ? partido.puntuacionVisitante : '?' }</p>
                </div>
                <div class="d-flex justify-content-end mt-3">
                    <a href="formularioPartido.html?id=${partido.id}" class="btn btn-sm btn-primary me-2">Editar</a>
                    <button class="btn btn-sm btn-danger delete-btn" data-id="${partido.id}">Eliminar</button>
                </div>
            </div>
        </a>
    `;
}