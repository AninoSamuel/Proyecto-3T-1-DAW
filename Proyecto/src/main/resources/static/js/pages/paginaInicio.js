import { crearBarraNavegacion } from '../components/navbar.js';

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('navbar').innerHTML = crearBarraNavegacion();

    const root = document.getElementById('root');

    root.innerHTML = `
        <header class="hero-section text-white text-center py-5">
            <div class="container">
                <h1 class="display-3 fw-bold mb-3">La Cancha es Tu Reino</h1>
                <p class="lead mb-4">Administra tu equipo, jugadores y logros con precisión de campeón.</p>
                <a class="btn btn-lg btn-hero-primary mt-3" href="listaJugadores.html" role="button">Empezar Ahora</a>
            </div>
        </header>

        <section class="features-section py-5 bg-light">
            <div class="container">
                <h2 class="text-center mb-5 feature-title">Lo que puedes hacer</h2>
                <div class="row g-4">
                    <div class="col-md-4">
                        <div class="feature-card p-4 text-center rounded shadow-sm h-100">
                            <i class="bi bi-person-fill feature-icon mb-3"></i> <h3 class="h5">Gestión de Partidos</h3>
                            <p class="text-muted">Ver Resulados de tus equipos favoritos </p>
                            <a href="listaPartidos.html" class="btn btn-sm btn-outline-dark mt-2">Ver Partidos</a>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="feature-card p-4 text-center rounded shadow-sm h-100">
                            <i class="bi bi-people-fill feature-icon mb-3"></i> <h3 class="h5">Administración de Equipos</h3>
                            <p class="text-muted">Organiza tus equipos, asigna jugadores y gestiona su información.</p>
                            <a href="listaEquipos.html" class="btn btn-sm btn-outline-dark mt-2">Ver Equipos</a>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="feature-card p-4 text-center rounded shadow-sm h-100">
                            <i class="bi bi-trophy-fill feature-icon mb-3"></i> <h3 class="h5">Seguimiento de Logros</h3>
                            <p class="text-muted">Registra y visualiza los logros de tus jugadores.</p>
                            <a href="listaLogros.html" class="btn btn-sm btn-outline-dark mt-2">Ver Logros</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="cta-section text-center py-5">
            <div class="container">
                <h2 class="mb-4 cta-title">¿Listo para llevar tu gestión al siguiente nivel?</h2>
                <p class="lead mb-4">Únete a nuestra plataforma y optimiza la administración de tu club.</p>
                <a class="btn btn-lg btn-cta-secondary" href="formularioJugador.html" role="button">Registrar Jugador</a>
            </div>
        </section>

        <footer class="footer py-4 text-center text-white-50">
            <div class="container">
                <p>&copy; ${new Date().getFullYear()} Gestión de Baloncesto. Todos los derechos reservados.</p>
            </div>
        </footer>
    `;
});