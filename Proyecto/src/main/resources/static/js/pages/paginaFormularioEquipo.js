import { crearBarraNavegacion } from '../components/navbar.js';
import { EquipoAPI } from '../api/equipoAPI.js';

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    const params = new URLSearchParams(window.location.search);
    const idEquipo = params.get('id');

    let equipoActual = {};

    if (idEquipo) {
        try {
            equipoActual = await EquipoAPI.getEquipoById(idEquipo);
            if (!equipoActual) {
                root.innerHTML = `<p class="text-danger text-center">Equipo con ID ${idEquipo} no encontrado.</p>`;
                return;
            }
            document.title = `Editar Equipo: ${equipoActual.nombre}`;
        } catch (error) {
            console.error('Error al cargar el equipo para edición:', error);
            root.innerHTML = `<p class="text-danger text-center">Error al cargar datos del equipo: ${error.message}</p>`;
            return;
        }
    } else {
        document.title = 'Crear Nuevo Equipo';
        equipoActual.anioFundacion = new Date().getFullYear();
    }

    root.innerHTML = `
    <div class="container mt-4">
        <h2 class="mb-4">${idEquipo ? 'Editar Equipo' : 'Crear Nuevo Equipo'}</h2>
        <form id="equipo-form">
            <div class="mb-3">
                <label for="nombre" class="form-label">Nombre del Equipo:</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="${equipoActual.nombre || ''}" required>
            </div>
            <div class="mb-3">
                <label for="ciudad" class="form-label">Ciudad:</label>
                <input type="text" class="form-control" id="ciudad" name="ciudad" value="${equipoActual.ciudad || ''}" required>
            </div>
            <div class="mb-3">
                <label for="abreviatura" class="form-label">Abreviatura (3 caracteres):</label>
                <input type="text" class="form-control" id="abreviatura" name="abreviatura" value="${equipoActual.abreviatura || ''}" maxlength="3" required>
            </div>
            <div class="mb-3">
                <label for="anioFundacion" class="form-label">Año de Fundación:</label>
                <input type="number" class="form-control" id="anioFundacion" name="anioFundacion" value="${equipoActual.anioFundacion || ''}" required min="1800" max="${new Date().getFullYear()}">
            </div>
            <div class="mb-3">
                <label for="pabellon" class="form-label">Pabellón:</label>
                <input type="text" class="form-control" id="pabellon" name="pabellon" value="${equipoActual.pabellon || ''}">
            </div>
            <div class="mb-3">
                <label for="entrenador" class="form-label">Entrenador:</label>
                <input type="text" class="form-control" id="entrenador" name="entrenador" value="${equipoActual.entrenador || ''}">
            </div>
            <button type="submit" class="btn btn-primary">Guardar Equipo</button>
            <a href="listaEquipos.html" class="btn btn-secondary">Volver a la Lista</a>
        </form>
    </div>`;

    const equipoForm = document.getElementById('equipo-form');

    equipoForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new FormData(equipoForm);
        const equipoData = {
            nombre: formData.get('nombre'),
            ciudad: formData.get('ciudad'),
            abreviatura: formData.get('abreviatura'),
            anioFundacion: parseInt(formData.get('anioFundacion')),
            pabellon: formData.get('pabellon'),
            entrenador: formData.get('entrenador')
        };

        console.log('Equipo Data a enviar (paginaFormularioEquipo.js):', equipoData);

        try {
            if (idEquipo) {
                await EquipoAPI.patchEquipo(idEquipo, equipoData);
                alert('Equipo actualizado exitosamente.');
            } else {
                await EquipoAPI.createEquipo(equipoData);
                alert('Equipo creado exitosamente.');
            }
            window.location.href = 'listaEquipos.html';
        } catch (error) {
            console.error('Error al guardar el equipo:', error);
            alert(`Error al guardar el equipo: ${error.message}`);
        }
    });
});