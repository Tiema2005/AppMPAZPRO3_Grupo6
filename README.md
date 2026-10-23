
# MPAZ PRO 3.º

## Descripción del proyecto

MPAZ PRO 3.º es una aplicación móvil educativa
desarrollada para estudiantes de Tercero Básico
de la Escuela Escritora Marcela Paz.

Su objetivo es ofrecer un entorno de aprendizaje
intuitivo, interactivo y adaptable a diferentes
tamaños de pantalla.

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Navigation Compose
- Arquitectura MVVM
- Kotlin Coroutines
- StateFlow
- DataStore Preferences
- FileProvider
- Activity Result API
- Git y GitHub

## Funcionalidades implementadas

### Bienvenida
- Pantalla inicial con diseño educativo.
- Diseño adaptable a distintos dispositivos.
- Navegación hacia el inicio de sesión.

### Registro de estudiantes
- Ingreso de nombre completo.
- Validación de correo electrónico.
- Validación y confirmación de contraseña.
- Almacenamiento de una cuenta local.
- Visualización de errores por campo.

### Inicio de sesión
- Validaciones centralizadas.
- Comprobación de credenciales locales.
- Mensajes de error.
- Navegación al perfil del estudiante.

### Perfil del estudiante
- Visualización de nombre y curso.
- Indicador de progreso demostrativo.
- Selección de fotografía desde la galería.
- Captura de fotografías mediante la cámara.
- Almacenamiento local de la fotografía.

### Asignaturas
- Lenguaje y Comunicación.
- Matemática.
- Ciencias Naturales.
- Historia y Geografía.
- Actividades interactivas de ejemplo.
- Retroalimentación de respuestas.

### Modo estudio
- Activación y desactivación.
- Animaciones visuales.
- Persistencia mediante DataStore.
- Retroalimentación al usuario.

## Arquitectura del proyecto

La aplicación utiliza una organización basada
en separación de responsabilidades.

app/src/main/java/com/example/appmpazpro3_grupo6/

- navigation: navegación y rutas.
- repository: acceso al almacenamiento local.
- ui/components: componentes reutilizables.
- ui/screens: pantallas de la aplicación.
- ui/theme: colores y estilos.
- ui/utils: utilidades.
- validation: validaciones de formularios.
- viewmodel: gestión de estados y lógica.

## Recursos nativos

La aplicación integra:

- Cámara: captura de fotografía de perfil.
- Galería: selección de una imagen existente.

Se utilizan contratos de Activity Result API
y FileProvider para gestionar las imágenes.

## Almacenamiento local

DataStore se utiliza para almacenar preferencias
y datos de una cuenta local del estudiante.

La contraseña no se almacena en texto plano:
se utiliza un verificador derivado con PBKDF2
y un salt aleatorio.

La fotografía se guarda en el almacenamiento
privado de la aplicación.

## Requisitos

- Android Studio.
- Dispositivo o emulador Android compatible.
- Android 13 (API 33) o superior.
- Gradle configurado mediante el proyecto.

## Ejecución

1. Clonar el repositorio desde GitHub.
2. Abrir el proyecto en Android Studio.
3. Sincronizar Gradle.
4. Seleccionar un emulador o dispositivo físico.
5. Ejecutar la aplicación con Run.

## Pruebas

Pruebas realizadas durante el desarrollo:

- Navegación entre pantallas.
- Validación de formularios.
- Registro de cuenta local.
- Inicio de sesión con credenciales.
- Acceso a asignaturas.
- Actividades de ejemplo.
- Visualización adaptable.

Pruebas pendientes de documentar:

- Cámara en dispositivo físico.
- Galería en dispositivo físico.
- Cancelación de cámara y galería.
- Persistencia de fotografía tras reiniciar.
- Pruebas automatizadas de validaciones.

## Limitaciones actuales

- Se admite una cuenta local por instalación.
- No existe sincronización entre dispositivos.
- No hay recuperación de contraseña.
- Las actividades son demostrativas.
- El progreso académico aún no es real.
- No se utiliza un servidor de autenticación.

## Gestión del proyecto

- Control de versiones: Git y GitHub.
- Planificación de tareas: Jira.
- Desarrollo colaborativo.
- Commits organizados por funcionalidades.

## Equipo de desarrollo

Proyecto académico de Desarrollo de
Aplicaciones Móviles.

Integrantes:
- Emanuel Toledo
- Felipe
- Ervin Millanguir

## Estado

Prototipo académico en desarrollo.

Las funcionalidades deben verificarse
antes de la entrega final.