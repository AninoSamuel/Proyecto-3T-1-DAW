
import { crearBarraNavegacion } from '../components/navbar.js';
import { PartidoAPI } from '../api/partidoAPI.js'; 
import { crearElementoPartido } from '../components/partidoComponente.js'; 

document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');
    root.innerHTML = `<h1 class="text-center my-4">Cargando Partidos...</h1>`;

    try {
        const partidos = await PartidoAPI.getAllPartidos(); 

        let partidosContainerHTML = ''; 
        if (partidos.length > 0) {
            partidosContainerHTML = '<div class="row">'; 
            for (const partido of partidos) {
                partidosContainerHTML += `<div class="col-md-6 col-lg-4 mb-4">
                                            ${crearElementoPartido(partido)}
                                          </div>`;
            }
            partidosContainerHTML += '</div>'; 
        } else {
            partidosContainerHTML = '<p class="text-center">No hay partidos registrados.</p>';
        }

        root.innerHTML = `
            <div class="container mt-4">
                <h1 class="display-4 mb-4">Gestión de Partidos</h1>
                <div class="d-flex justify-content-end mb-3">
                    <a href="formularioPartido.html" class="btn btn-success">Añadir Nuevo Partido</a>
                </div>
                ${partidosContainerHTML} </div>`;

        document.querySelectorAll('.btn-danger').forEach(button => {
            button.addEventListener('click', async (event) => {
                const partidoId = event.target.dataset.id;
                if (confirm(`¿Está seguro de que desea eliminar el partido con ID ${partidoId}?`)) {
                    try {
                        await PartidoAPI.deletePartido(partidoId); 
                        alert('Partido eliminado exitosamente.');
                        window.location.reload(); 
                    } catch (error) {
                        console.error('Error al eliminar el partido:', error);
                        alert(`Error al eliminar el partido: ${error.message}`);
                    }
                }
            });
        });

    } catch (error) {
        console.error('Error al cargar la lista de partidos:', error);
        root.innerHTML = `<p class="text-danger text-center">Error al cargar partidos: ${error.message}</p>`;
    }
});