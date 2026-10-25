# MPAZ PRO 3.º

**Aplicación móvil educativa para estudiantes de Tercero Básico**

**Proyecto académico:** Desarrollo de Aplicaciones Móviles — DUOC UC  
**Caso educativo:** Escuela Escritora Marcela Paz  
**Plataforma:** Android  
**Lenguaje:** Kotlin  
**Estado:** MVP académico funcional

## 1. Descripción del proyecto

MPAZ PRO 3.º es una aplicación móvil educativa orientada a estudiantes de Tercero Básico de la Escuela Escritora Marcela Paz.

Su propósito es facilitar el aprendizaje mediante actividades interactivas de Lenguaje y Comunicación, Matemática, Ciencias Naturales e Historia y Geografía.

La aplicación incorpora formularios validados, navegación entre pantallas, ejercicios de dificultad progresiva, explicaciones de respuestas, modo estudio, seguimiento de progreso y rendimiento, almacenamiento local y acceso a recursos del dispositivo.

Se desarrolló como un prototipo académico, utilizando datos y actividades de demostración.

## 2. Objetivo

Ofrecer una experiencia educativa sencilla, visual y accesible que permita al estudiante practicar contenidos, recibir retroalimentación inmediata y consultar su avance de aprendizaje.

## 3. Tecnologías utilizadas

- Android Studio.
- Kotlin.
- Jetpack Compose.
- Material Design 3.
- Navigation Compose.
- Arquitectura MVVM.
- ViewModel y StateFlow.
- Kotlin Coroutines.
- DataStore Preferences.
- FileProvider.
- Activity Result API.
- Git y GitHub.
- Jira para planificación y seguimiento.
- JUnit para pruebas automatizadas.

## 4. Funcionalidades implementadas

### 4.1. Pantalla de bienvenida

- Presentación inicial de MPAZ PRO 3.º.
- Diseño visual educativo.
- Mascota y elementos gráficos.
- Adaptación a distintos tamaños de pantalla mediante Window Size Classes.
- Botón para comenzar y acceder al inicio de sesión.

### 4.2. Registro de estudiantes

- Ingreso de nombre completo y correo electrónico.
- Contraseña y confirmación de contraseña.
- Validación de campos obligatorios.
- Validación del formato de correo.
- Validación de longitud mínima y coincidencia de contraseñas.
- Mensajes visuales de error.
- Registro de una cuenta local.
- Curso predeterminado: 3.º Básico.

### 4.3. Inicio de sesión

- Validación de correo y contraseña.
- Verificación de las credenciales guardadas localmente.
- Retroalimentación cuando los datos son incorrectos.
- Recuperación de nombre y curso desde la cuenta registrada.
- Navegación hacia el perfil del estudiante.

### 4.4. Perfil del estudiante

- Visualización de nombre y curso.
- Imagen de perfil personalizada.
- Selección de fotografías desde la galería.
- Captura de fotografías mediante la cámara del dispositivo.
- Conservación local de la fotografía.
- Visualización del progreso general de aprendizaje.
- Porcentaje de avance por asignatura.
- Número de ejercicios completados.
- Rendimiento general de respuestas correctas.
- Rendimiento por asignatura.
- Acceso a las asignaturas y al modo estudio.

### 4.5. Asignaturas

La aplicación incorpora cuatro asignaturas:

1. Lenguaje y Comunicación.
2. Matemática.
3. Ciencias Naturales.
4. Historia y Geografía.

Cada asignatura contiene tres ejercicios organizados por dificultad:

- Nivel fácil.
- Nivel medio.
- Nivel difícil.

Cada ejercicio presenta una pregunta y alternativas de respuesta.

Después de responder, el sistema muestra si la respuesta es correcta o incorrecta, indica la alternativa correcta cuando corresponde y entrega una explicación educativa.

Al terminar los tres ejercicios se muestra un resumen de la práctica, con la cantidad de aciertos y el rendimiento obtenido.

El estudiante puede volver a practicar.

### 4.6. Modo estudio

El modo estudio es una herramienta de apoyo al aprendizaje.

Permite:

- Activar o desactivar una preferencia de estudio.
- Guardar la configuración mediante DataStore.
- Mostrar pistas antes de responder los ejercicios.
- Presentar consejos adicionales después de responder.
- Mantener las explicaciones de las respuestas.
- Mostrar animaciones y retroalimentación visual al cambiar su estado.

El modo estudio no modifica las respuestas correctas ni el cálculo del avance. Su finalidad es entregar apoyo durante la práctica.

## 5. Progreso real de aprendizaje

La aplicación registra el progreso mediante `ProgresoRepository.kt` y `ProgresoViewModel.kt`.

Existen dos indicadores diferentes.

### 5.1. Avance

El avance indica la cantidad de ejercicios completados.

Cada asignatura tiene tres ejercicios y el proyecto tiene doce en total.

**Fórmula por asignatura:**

Porcentaje de avance = ejercicios completados / 3 × 100

**Fórmula general:**

Progreso general = ejercicios completados / 12 × 100

El avance se conserva entre sesiones y no supera el 100 %.

### 5.2. Rendimiento

El rendimiento indica cuántas respuestas fueron correctas.

Para calcularlo, el sistema considera la última respuesta registrada de cada ejercicio que tenga una calificación disponible.

**Fórmula:**

Rendimiento = respuestas correctas / ejercicios evaluados × 100

El porcentaje se redondea al entero más cercano.

**Ejemplo de Matemática:**

Un estudiante responde los tres ejercicios, con dos respuestas correctas y una incorrecta.

- Ejercicios completados: 3 de 3.
- Avance: 100 %.
- Respuestas correctas: 2 de 3.
- Rendimiento: 67 %.

Si el estudiante vuelve a practicar y mejora su respuesta, el rendimiento se actualiza con el último resultado del ejercicio.

### 5.3. Historial de intentos

Para cada ejercicio, el sistema almacena:

- Número total de intentos.
- Últimos 30 resultados de acierto o error.
- Último resultado registrado.

La información se utiliza para el cálculo de rendimiento.

El historial almacena resultados booleanos de correcto o incorrecto. No conserva el texto de todas las alternativas seleccionadas.

Los avances guardados antes de implementar el historial pueden conservar su porcentaje de avance, aunque no dispongan de calificaciones históricas.

## 6. Persistencia y seguridad local

La aplicación utiliza DataStore Preferences para conservar:

- Datos de la cuenta local.
- Preferencia del modo estudio.
- Avances y resultados de ejercicios.

En `CuentaLocalRepository.kt`, la contraseña no se guarda en texto plano. Se utiliza PBKDF2 con HMAC-SHA256 y una sal aleatoria para generar el verificador de contraseña.

La fotografía del perfil se almacena en archivos privados de la aplicación y su referencia se conserva en preferencias locales.

La aplicación no utiliza un servidor para autenticar usuarios ni sincronizar datos entre dispositivos.

Las reglas de respaldo de Android se configuran para evitar copias automáticas no deseadas de los datos locales.

## 7. Recursos nativos del dispositivo

### Cámara

Permite tomar una fotografía para el perfil utilizando `ActivityResultContracts.TakePicture` y `FileProvider`.

### Galería

Permite seleccionar una fotografía existente mediante `ActivityResultContracts.GetContent`.

Ambos recursos están integrados en el flujo del perfil del estudiante.

## 8. Arquitectura y organización

El proyecto utiliza separación de responsabilidades basada en MVVM.

**Paquete principal:**

`com.example.appmpazpro3_grupo6`

**Estructura:**

- `navigation/`: rutas y navegación entre pantallas.
- `repository/`: acceso y persistencia de datos locales.
- `ui/components/`: componentes visuales reutilizables.
- `ui/screens/`: pantallas de la aplicación.
- `ui/theme/`: colores, tipografía y estilos.
- `ui/utils/`: utilidades, incluido FileProvider.
- `validation/`: reglas de validación de formularios.
- `viewmodel/`: estado y coordinación de lógica de las pantallas.
- `MainActivity.kt`: actividad principal.

**Componentes principales:**

- `AppNavigation.kt`
- `CuentaLocalRepository.kt`
- `EstadoDataStore.kt`
- `ProgresoRepository.kt`
- `CuentaViewModel.kt`
- `EstadoViewModel.kt`
- `EstudianteViewModel.kt`
- `PerfilImagenViewModel.kt`
- `ProgresoViewModel.kt`
- `ValidadorFormulario.kt`

La interfaz está desarrollada con Jetpack Compose y reutiliza componentes para botones, tarjetas, encabezados, fondos, textos y barras de progreso.

## 9. Requisitos de ejecución

- Android Studio compatible con el proyecto.
- Android SDK y Gradle configurados.
- Dispositivo físico o emulador con Android 13 (API 33) o superior.
- Kotlin y dependencias administradas por Gradle.

**Configuración del módulo:**

- `minSdk`: 33.
- `targetSdk`: 37.
- `compileSdk`: 37.
- `versionName`: 1.0.

## 10. Cómo ejecutar la aplicación

1. Clonar o descargar el repositorio de GitHub.
2. Abrir la carpeta principal del proyecto en Android Studio.
3. Esperar la sincronización de Gradle.
4. Comprobar que el SDK de Android esté instalado.
5. Seleccionar un emulador o conectar un dispositivo físico.
6. Ejecutar el módulo `app` con el botón Run.
7. Registrar una cuenta local.
8. Iniciar sesión y acceder al perfil.
9. Seleccionar una asignatura.
10. Completar los ejercicios y consultar el avance y rendimiento.
11. Activar el modo estudio para practicar con pistas y consejos.

**Repositorio:**

https://github.com/Tiema2005/AppMPAZPRO3_Grupo6

## 11. Pruebas y validación

El proyecto incorpora pruebas automatizadas de validación de formularios y contempla pruebas unitarias para el modelo de progreso y rendimiento.

### Pruebas instrumentadas de formularios

Archivo:

`app/src/androidTest/java/com/example/appmpazpro3_grupo6/validation/ValidadorFormularioTest.kt`

Incluye siete casos sobre:

- Correo vacío en login.
- Formato de correo incorrecto.
- Contraseña demasiado corta.
- Login con formato válido.
- Registro sin nombre.
- Contraseñas diferentes en registro.
- Registro con datos válidos.

### Pruebas unitarias de progreso

Archivo:

`app/src/test/java/com/example/appmpazpro3_grupo6/repository/ProgresoEstudianteTest.kt`

Verifica los cálculos de avance y rendimiento, límites de porcentajes, últimas respuestas y casos sin calificación.

### Comandos de prueba

Para las pruebas unitarias:

`.\gradlew.bat :app:testDebugUnitTest`

Para las pruebas instrumentadas con un dispositivo conectado:

`.\gradlew.bat :app:connectedDebugAndroidTest`

### Pruebas funcionales manuales

Durante el desarrollo se han probado en Android Studio y dispositivos Android:

- Navegación entre pantallas.
- Registro e inicio de sesión.
- Validaciones visuales.
- Actividades de las cuatro asignaturas.
- Explicaciones y respuestas correctas.
- Progreso real y rendimiento.
- Modo estudio.
- Cámara y galería.
- Diseño adaptable.

Los resultados de las ejecuciones automáticas finales deben documentarse con capturas antes de la entrega.

## 12. Limitaciones y alcance

MPAZ PRO 3.º es un MVP académico, no una plataforma escolar productiva.

Limitaciones:

- Admite una cuenta local por instalación.
- El progreso está almacenado por instalación y todavía no se separa por cuenta.
- No existe sincronización entre dispositivos.
- No hay recuperación automática de contraseñas.
- Las actividades educativas son ejemplos de alcance limitado.
- No existe un módulo docente completo de aprobación de contenidos.
- No se generan diagnósticos académicos automáticos.
- Los datos utilizados para las demostraciones deben ser ficticios.

Las mejoras futuras podrían incluir más unidades educativas, seguimiento individual por cuenta, nuevas actividades y funciones docentes, sujetas a los requerimientos del proyecto.

## 13. Gestión colaborativa

El desarrollo se ha realizado mediante trabajo colaborativo, utilizando:

- Git para control de versiones.
- GitHub para almacenamiento del código e historial de commits.
- Ramas y pull requests para integración de cambios.
- Jira para planificación, asignación y seguimiento de tareas.

Las evidencias del trabajo incluyen el historial de commits, las tareas asignadas, las pruebas y las capturas funcionales.

## 14. Equipo de desarrollo

**Proyecto MPAZ PRO 3.º — Desarrollo de Aplicaciones Móviles**

- Emanuel Toledo.
- Felipe Escobar.
- Ervin Millanguir.

## 15. Estado final

Versión académica funcional con interfaz desarrollada en Jetpack Compose, autenticación local, actividades interactivas, progresión de dificultad, correcciones educativas, modo estudio, recursos nativos y seguimiento persistente del aprendizaje.

La versión de entrega se considera preparada una vez aprobadas las pruebas finales, comprobada la compilación y registrado el commit definitivo dentro del plazo establecido.
