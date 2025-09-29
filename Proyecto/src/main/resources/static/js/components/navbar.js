
export function crearBarraNavegacion() {
    return `
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4">
            <div class="container-fluid">
                <a class="navbar-brand text-light" href="index.html">Gestión de Baloncesto</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item">
                            <a class="nav-link text-light" href="index.html">Inicio</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-light" href="listaEquipos.html">Equipos</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-light" href="listaJugadores.html">Jugadores</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-light" href="listaPartidos.html">Partidos</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-light" href="listaLogros.html">Logros</a>
                        </li>
                        </ul>
                </div>
            </div>
        </nav>
    `;
}