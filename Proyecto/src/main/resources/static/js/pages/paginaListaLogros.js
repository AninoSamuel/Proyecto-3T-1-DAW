import { crearBarraNavegacion } from '../components/navbar.js';
import { LogroAPI } from '../api/logroAPI.js';
import { JugadorAPI } from '../api/jugadorAPI.js';
import { crearElementoLogro } from '../components/logroComponente.js';

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    root.innerHTML = `<h1 class="text-center my-4">Cargando Logros...</h1>`;

    try {
        const logros = await LogroAPI.getAllLogros();
        const jugadores = await JugadorAPI.getAllJugadores();

        let listaLogrosHTML = '';
        if (logros.length > 0) {
            for (const logro of logros) {
                listaLogrosHTML += `<li class="list-group-item d-flex justify-content-between align-items-center">
                                        ${crearElementoLogro(logro, jugadores)} 
                                    </li>`;
            }
        } else {
            listaLogrosHTML = '<li class="list-group-item text-center">No hay logros registrados.</li>';
        }

        root.innerHTML = `
            <div class="container mt-4">
                <h1 class="display-4 mb-4">Gestión de Logros</h1>
                <div class="d-flex justify-content-end mb-3">
                    <a href="formularioLogro.html" class="btn btn-success">Añadir Nuevo Logro</a>
                </div>
                <ul class="list-group">
                    ${listaLogrosHTML}
                </ul>
            </div>`;

        document.querySelectorAll('.btn-danger').forEach(button => {
            button.addEventListener('click', async (event) => {
                const logroId = event.target.dataset.id;
                if (confirm(`¿Está seguro de que desea eliminar el logro con ID ${logroId}?`)) {
                    try {
                        await LogroAPI.deleteLogro(logroId);
                        alert('Logro eliminado exitosamente.');
                        window.location.reload();
                    } catch (error) {
                        console.error('Error al eliminar el logro:', error);
                        alert(`Error al eliminar el logro: ${error.message}`);
                    }
                }
            });
        });

    } catch (error) {
        console.error('Error al cargar la lista de logros:', error);
        root.innerHTML = `<p class="text-danger text-center">Error al cargar logros: ${error.message}</p>`;
    }
});