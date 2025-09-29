[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/nEmZ8A4d)


## ¿Qué necesitas tener en tu ordenador?

Antes de poder usar esta app hecha con Spring Boot, necesitas tener estas cosas instaladas:

-**Java JDK 17 o más nuevo**  
  > Es el lenguaje en el que está programado todo. Si no lo tienes, búscalo como “descargar Java JDK 17” y sigue los pasos para instalarlo.

-**Maven 3.6 o más**  
  > Es como el ayudante que prepara, construye y lanza el proyecto. Lo puedes descargar desde [https://maven.apache.org/](https://maven.apache.org/).

-**Un editor o IDE**  
  > Puedes usar cualquiera de estos (elige el que te guste más):  
  - [IntelliJ IDEA](https://www.jetbrains.com/es-es/idea/)  
  - [Eclipse](https://www.eclipse.org/)  
  - [VS Code](https://code.visualstudio.com/)  

-**Git (opcional)**  
  > Si quieres clonar el proyecto desde GitHub en vez de descargarlo a mano. No es obligatorio, pero es útil.

-**Para generar la documentación del código (Javadoc):**  
  > Maven ya lo trae listo para generar Javadoc. Solo tienes que usar este comando en la terminal dentro del proyecto:


## ¿Cómo iniciar el programa?

Sigue estos pasos para ejecutar la aplicación Spring Boot:

1. **Clona o descarga el repositorio**
    - Si tienes Git:
      ```
      git clone https://github.com/usuario/repo
      ```
    - O descarga el ZIP desde GitHub y descomprímelo.

2. **Abre el proyecto en tu IDE**
    - Usa VS Code para abrir la carpeta del proyecto.

3. **Instala las dependencias**
    - Abre una terminal en la carpeta raíz del proyecto y ejecuta:
      ```
      mvn clean install
      ```
    - Esto descargará todas las dependencias necesarias.

4. **Ejecuta la aplicación**
    - Desde la terminal, ejecuta:
      ```
      mvn spring-boot:run
      ```
    - O, si prefieres, ejecuta la clase principal (`main/ProyectoApplication`) desde tu IDE.

5. **Accede a la aplicación**
    - Por defecto, estará disponible en tu navegador: (http://localhost:8080)

6. **Para generar la documentación (Javadoc)**
    ```
    - mvn javadoc:javadoc
    ```

    ## Rutas relevantes de la API

    A continuación se describen las rutas principales expuestas por la API de la aplicación:

    (Mas acompañados antes de un http://localhost:8080)

    ### Jugadores

    - **GET `/api/v1/jugadores`**  
        Obtiene la lista de todos los jugadores registrados.

    - **GET `/api/v1/jugadores/{id}`**  
        Obtiene la información de un jugador específico por su ID.

    - **POST `/api/v1/jugadores`**  
        Crea un nuevo jugador.  

        _Cuerpo esperado:_  

        ```json
        {
            "id": 1,
            "nombre": "Tomás",
            "apellido": "Ávila",
            "fechaNacimiento": "1992-08-01",
            "alturaCm": 203,
            "pesoKg": 103,
            "posicion": "Alero",
            "numeroCamiseta": 64,
            "activo": false
        }
        ```

    - **PUT `/api/v1/jugadores/{id}`**  
        Actualiza los datos de un jugador existente.

    - **PATCH `/api/v1/jugadores/{id}`**  
    Actualiza un jugador existente pero solo algunos campos.

    - **DELETE `/api/v1/jugadors/{id}`**  
        Elimina un jugador por su ID.

    ### Ejemplo de otra entidad

    - **GET `/api/v1/equipos`**  
        Lista todos los equipos.

    - **GET `/api/v1/equipos/{id}`**  
        Detalle de un equipo específico.

    - **POST `/api/v1/equipos`**  
        Crea un nuevo equipo.

    - **PUT `/api/v1/equipos/{id}`**  
        Actualiza un equipo existente.

    - **PATCH `/api/v1/equipos/{id}`**  
        Actualiza un equipo existente pero solo algunos campos.

    - **DELETE `/api/v1/equipos/{id}`**  
        Elimina un equipo.


        ## ¿Cómo acceder a la consola H2?

        La aplicación utiliza una base de datos H2 en memoria para facilitar el desarrollo y las pruebas. Puedes acceder a la consola web de H2 para consultar y modificar los datos directamente desde tu navegador.

        Sigue estos pasos para acceder a la consola H2:

        1. **Asegúrate de que la aplicación esté en ejecución**  
            Ejecuta el comando:
            ```
            mvn spring-boot:run
            ```
            o inicia la aplicación desde tu IDE.

        2. **Abre tu navegador y accede a la consola H2**  
            Ve a la siguiente URL:
            ```
            http://localhost:8080/h2-console
            ```

        3. **Configura la conexión**  
            En la pantalla de inicio de sesión de H2, asegúrate de que los campos estén así:
            - **JDBC URL:**  
              ```
                jdbc:h2:mem:db;DB_CLOSE_DELAY=-1          
                ```
            - **User Name:**  
              ```
              sa (En mi caso)
              ```
            - **Password:**  
              (deja este campo vacío, a menos que hayas configurado una contraseña diferente)

        4. **Haz clic en "Connect"**  
            Ahora podrás ver y consultar las tablas de la base de datos desde la interfaz web.
S