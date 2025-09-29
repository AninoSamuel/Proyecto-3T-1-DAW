export function crearElementoEquipo(equipo) {
    const nombre = equipo.nombre || 'Nombre desconocido';
    const ciudad = equipo.ciudad || 'Ciudad desconocida';
    const entrenador = equipo.entrenador || 'N/A';
    const anioFundacion = equipo.anioFundacion || 'N/A';
    const id = equipo.id; 

    return `
        <div class="card h-100 shadow-sm">
            <div class="card-body d-flex flex-column">
                <h5 class="card-title text-primary mb-2">${nombre}</h5>
                <h6 class="card-subtitle mb-2 text-muted">${ciudad}</h6>
                <hr>
                <p class="card-text mb-1">
                    <strong><i class="fas fa-user-tie me-2"></i>Entrenador:</strong> ${entrenador}
                </p>
                <p class="card-text mb-3">
                    <strong><i class="far fa-calendar-alt me-2"></i>Año:</strong> ${anioFundacion}
                </p>

                <div class="mt-auto d-flex justify-content-between align-items-center">
                    <a href="detalleEquipo.html?id=${id}" class="btn btn-outline-info btn-sm flex-grow-1 me-2">
                        <i class="fas fa-info-circle me-1"></i> Ver Detalles
                    </a>
                    <a href="formularioEquipo.html?id=${id}" class="btn btn-primary btn-sm flex-grow-1 me-2">
                        <i class="fas fa-edit me-1"></i> Editar
                    </a>
                    <button class="btn btn-danger btn-sm delete-btn flex-grow-1" data-id="${id}">
                        <i class="fas fa-trash-alt me-1"></i> Eliminar
                    </button>
                </div>
            </div>
        </div>
    `;
}       