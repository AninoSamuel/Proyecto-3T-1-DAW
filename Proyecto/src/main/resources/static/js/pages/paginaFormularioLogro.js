
import { crearBarraNavegacion } from '../components/navbar.js';
import { LogroAPI } from '../api/logroAPI.js';

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    const params = new URLSearchParams(window.location.search);
    const idLogro = params.get('id');

    let logroActual = {};

    if (idLogro) {
        try {
            logroActual = await LogroAPI.getLogroById(idLogro); 
            if (!logroActual) {
                root.innerHTML = `<p class="text-danger text-center">Logro con ID ${idLogro} no encontrado.</p>`;
                return;
            }
            document.title = `Editar Logro: ${logroActual.nombre}`;
        } catch (error) {
            console.error('Error al cargar el logro para edición:', error);
            root.innerHTML = `<p class="text-danger text-center">Error al cargar datos del logro: ${error.message}</p>`;
            return;
        }
    } else {
        document.title = 'Crear Nuevo Logro';
    }

    root.innerHTML = `
    <div class="container mt-4">
        <h2 class="mb-4">${idLogro ? 'Editar Logro' : 'Crear Nuevo Logro'}</h2>
        <form id="logro-form">
            <div class="mb-3">
                <label for="nombre" class="form-label">Nombre del Logro:</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${logroActual.nombre || ''}" required>
            </div>
            <button type="submit" class="btn btn-primary">Guardar Logro</button>
            <a href="listaLogros.html" class="btn btn-secondary">Volver a la Lista</a>
        </form>
    </div>`;

    const logroForm = document.getElementById('logro-form');

    logroForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new FormData(logroForm);
        const logroData = {
            nombre: formData.get('nombre'),
            descripcion: formData.get('descripcion'),
        };

        try {
            if (idLogro) {
                await LogroAPI.updateLogro(idLogro, logroData); 
                alert('Logro actualizado exitosamente.');
            } else {
                await LogroAPI.createLogro(logroData);
                alert('Logro creado exitosamente.');
            }
            window.location.href = 'listaLogros.html';
        } catch (error) {
            console.error('Error al guardar el logro:', error);
            alert(`Error al guardar el logro: ${error.message}`);
        }
    });
});