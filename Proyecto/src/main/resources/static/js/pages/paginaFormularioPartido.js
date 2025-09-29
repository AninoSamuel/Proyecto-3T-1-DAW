import { crearBarraNavegacion } from '../components/navbar.js';
import { PartidoAPI } from '../api/partidoAPI.js';
import { EquipoAPI } from '../api/equipoAPI.js';

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    const params = new URLSearchParams(window.location.search);
    const idPartido = params.get('id'); 
    let partidoActual = null; 
    let todosLosEquipos = []; 

    try {
        todosLosEquipos = await EquipoAPI.getAllEquipos();
    } catch (error) {
        console.error('Error al cargar los equipos:', error);
        root.innerHTML = `
            <div class="container mt-4">
                <p class="alert alert-danger text-center">
                    <i class="fas fa-exclamation-triangle me-2"></i>Error al cargar equipos para los selectores: ${error.message}
                </p>
                <a href="listaPartidos.html" class="btn btn-secondary mt-3">Volver a la Lista de Partidos</a>
            </div>
        `;
        return; 
    }

    if (idPartido) {
        try {
            partidoActual = await PartidoAPI.getPartidoById(idPartido);
            if (!partidoActual) {
                root.innerHTML = `
                    <div class="container mt-4">
                        <p class="alert alert-warning text-center">
                            <i class="fas fa-exclamation-circle me-2"></i>Partido con ID ${idPartido} no encontrado.
                        </p>
                        <a href="listaPartidos.html" class="btn btn-secondary mt-3">Volver a la Lista de Partidos</a>
                    </div>
                `;
                return;
            }
            document.title = `Editar Partido: ${partidoActual.equipoLocal?.nombre || 'N/A'} vs ${partidoActual.equipoVisitante?.nombre || 'N/A'}`;
        } catch (error) {
            console.error('Error al cargar el partido para edición:', error);
            root.innerHTML = `
                <div class="container mt-4">
                    <p class="alert alert-danger text-center">
                        <i class="fas fa-times-circle me-2"></i>Error al cargar datos del partido: ${error.message}
                    </p>
                    <a href="listaPartidos.html" class="btn btn-secondary mt-3">Volver a la Lista de Partidos</a>
                </div>
            `;
            return; 
        }
    } else {
        document.title = 'Crear Nuevo Partido';
    }

    root.innerHTML = `
        <div class="container mt-4">
            <h2 class="mb-4 text-center">${idPartido ? 'Editar Partido' : 'Crear Nuevo Partido'}</h2>
            <form id="partido-form" class="p-4 border rounded shadow-sm">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="fecha" class="form-label"><i class="far fa-calendar-alt me-2"></i>Fecha:</label>
                        <input type="date" class="form-control" id="fecha" name="fecha"
                               value="${partidoActual?.fecha ? new Date(partidoActual.fecha).toISOString().split('T')[0] : ''}" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="hora" class="form-label"><i class="far fa-clock me-2"></i>Hora:</label>
                        <input type="time" class="form-control" id="hora" name="hora"
                               value="${partidoActual?.hora || ''}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="lugar" class="form-label"><i class="fas fa-map-marker-alt me-2"></i>Lugar:</label>
                    <input type="text" class="form-control" id="lugar" name="lugar"
                           value="${partidoActual?.lugar || ''}" placeholder="Ej: Rocket Mortgage FieldHouse" required>
                </div>

                <div class="mb-3">
                    <label for="temporada" class="form-label"><i class="fas fa-calendar-check me-2"></i>Temporada:</label>
                    <input type="text" class="form-control" id="temporada" name="temporada"
                           value="${partidoActual?.temporada || ''}" placeholder="Ej: 2024-2025" required>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="equipoLocalId" class="form-label"><i class="fas fa-home me-2"></i>Equipo Local:</label>
                        <select class="form-select" id="equipoLocalId" name="equipoLocalId" required>
                            <option value="">Seleccione equipo local</option>
                            ${todosLosEquipos.map(equipo => `
                                <option value="${equipo.id}" ${partidoActual?.equipoLocal?.id == equipo.id ? 'selected' : ''}>
                                    ${equipo.nombre}
                                </option>
                            `).join('')}
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="equipoVisitanteId" class="form-label"><i class="fas fa-plane-departure me-2"></i>Equipo Visitante:</label>
                        <select class="form-select" id="equipoVisitanteId" name="equipoVisitanteId" required>
                            <option value="">Seleccione equipo visitante</option>
                            ${todosLosEquipos.map(equipo => `
                                <option value="${equipo.id}" ${partidoActual?.equipoVisitante?.id == equipo.id ? 'selected' : ''}>
                                    ${equipo.nombre}
                                </option>
                            `).join('')}
                        </select>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="puntuacionLocal" class="form-label"><i class="fas fa-basketball-ball me-2"></i>Puntos Local:</label>
                        <input type="number" class="form-control" id="puntuacionLocal" name="puntuacionLocal"
                               value="${partidoActual?.puntuacionLocal !== undefined ? partidoActual.puntuacionLocal : 0}" min="0">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="puntuacionVisitante" class="form-label"><i class="fas fa-basketball-ball me-2"></i>Puntos Visitante:</label>
                        <input type="number" class="form-control" id="puntuacionVisitante" name="puntuacionVisitante"
                               value="${partidoActual?.puntuacionVisitante !== undefined ? partidoActual.puntuacionVisitante : 0}" min="0">
                    </div>
                </div>

                <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                    <button type="submit" class="btn btn-primary btn-lg">
                        <i class="fas fa-save me-2"></i>${idPartido ? 'Guardar Cambios' : 'Crear Partido'}
                    </button>
                    <a href="listaPartidos.html" class="btn btn-secondary btn-lg">
                        <i class="fas fa-arrow-left me-2"></i>Volver a la Lista
                    </a>
                </div>
            </form>
        </div>
    `;

    const partidoForm = document.getElementById('partido-form');

    partidoForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new FormData(partidoForm);


        const equipoLocalSeleccionadoId = parseInt(formData.get('equipoLocalId'));
        const equipoVisitanteSeleccionadoId = parseInt(formData.get('equipoVisitanteId'));

        const equipoLocalData = todosLosEquipos.find(equipo => equipo.id === equipoLocalSeleccionadoId);
        const equipoVisitanteData = todosLosEquipos.find(equipo => equipo.id === equipoVisitanteSeleccionadoId);

        const partidoData = {
            ...(idPartido && { id: parseInt(idPartido) }), 

            fecha: formData.get('fecha'),
            hora: formData.get('hora'),
            lugar: formData.get('lugar'),
            temporada: formData.get('temporada'), 
            puntuacionLocal: parseInt(formData.get('puntuacionLocal')),
            puntuacionVisitante: parseInt(formData.get('puntuacionVisitante')),
            equipoLocal: equipoLocalData, 
            equipoVisitante: equipoVisitanteData 
        };

        console.log('Datos del partido a enviar:', partidoData);

        try {
            if (idPartido) {
                await PartidoAPI.updatePartido(idPartido, partidoData); 
                alert('Partido actualizado exitosamente.');
            } else {
                await PartidoAPI.createPartido(partidoData);
                alert('Partido creado exitosamente.');
            }
            window.location.href = 'listaPartidos.html'; 
        } catch (error) {
            console.error('Error al guardar el partido:', error);
            alert(`Error al guardar el partido: ${error.message || 'Ocurrió un error desconocido.'}`);
        }
    });
});