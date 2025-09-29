
import { crearBarraNavegacion } from '../components/navbar.js';
import { EquipoAPI } from '../api/equipoAPI.js'; 
import { crearElementoEquipo } from '../components/equipoComponente.js';

document.addEventListener('DOMContentLoaded', async () => {
    console.log('1. DOMContentLoadado. Iniciando carga de página.');

    document.getElementById('navbar').innerHTML = crearBarraNavegacion();
    console.log('2. Barra de navegación inyectada.');

    const rootDiv = document.getElementById('root');
    rootDiv.innerHTML = `
        <div class="container mt-4">
            <h2 class="mb-4">Lista de Equipos</h2>
            <div class="d-flex justify-content-end mb-3">
                <a href="formularioEquipo.html" class="btn btn-primary">Crear Nuevo Equipo</a>
            </div>
            <div id="equipos-list" class="row">
                <p class="text-center">Cargando equipos...</p>
            </div>
        </div>
    `;
    console.log('3. Estructura base de la página inyectada. Mensaje "Cargando..." visible.');

    const equiposListDiv = document.getElementById('equipos-list');

    try {
        console.log('4. Intentando llamar a EquipoAPI.getAllEquipos().'); 
        const equipos = await EquipoAPI.getAllEquipos();
        console.log('5. Datos de equipos recibidos:', equipos);

        equiposListDiv.innerHTML = '';

        if (equipos.length === 0) {
            equiposListDiv.innerHTML = '<p class="text-center">No hay equipos registrados.</p>';
            console.log('6. No hay equipos para mostrar.');
            return;
        }

        equipos.forEach(equipo => {
            const colDiv = document.createElement('div');
            colDiv.className = 'col-md-6 col-lg-4';
            colDiv.innerHTML = crearElementoEquipo(equipo);
            equiposListDiv.appendChild(colDiv);
        });
        console.log('7. Equipos renderizados en la página.');

        equiposListDiv.addEventListener('click', async (event) => {
            if (event.target.classList.contains('delete-btn')) {
                const equipoId = event.target.dataset.id;
                if (confirm(`¿Estás seguro de que quieres eliminar el equipo con ID ${equipoId}?`)) {
                    try {
                        await EquipoAPI.deleteEquipo(equipoId); 
                        alert('Equipo eliminado exitosamente.');
                        location.reload();
                    } catch (error) {
                        console.error('Error al eliminar el equipo:', error);
                        alert(`Error al eliminar el equipo: ${error.message}`);
                    }
                }
            }
        });
        console.log('8. Event listeners de eliminar añadidos.');

    } catch (error) {
        console.error('Error al cargar los equipos:', error);
        equiposListDiv.innerHTML = `<p class="text-danger text-center">Error al cargar los equipos: ${error.message}</p>`;
    }
    console.log('9. Fin del script de paginaListaEquipos.js.');
});