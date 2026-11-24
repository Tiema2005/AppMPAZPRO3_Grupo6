# 🦉 MPAZ PRO 3.º

**Aplicación móvil educativa para estudiantes de Tercero Básico**

**Duoc UC | Desarrollo de Aplicaciones Móviles | Grupo 6**

**Versión:** 1.0  
**Plataforma:** Android  
**Estado:** Versión académica con APK release firmado y pruebas automatizadas.

---

## 1. Descripción del proyecto

**MPAZ PRO 3.º** es una aplicación móvil educativa desarrollada para estudiantes de Tercero Básico de la Escuela Escritora Marcela Paz.

Su propósito es proporcionar una experiencia de aprendizaje interactiva, intuitiva y accesible, permitiendo a los estudiantes reforzar sus conocimientos mediante ejercicios organizados por asignatura y nivel de dificultad.

La aplicación incorpora una interfaz desarrollada con Jetpack Compose, navegación entre pantallas, registro e inicio de sesión, seguimiento del progreso académico, metas de estudio e integración con contenido educativo de Wikipedia.

El proyecto utiliza una arquitectura MVVM en Android y dispone de integración con un backend desarrollado mediante Spring Boot y PostgreSQL.

## 2. Objetivo general

Desarrollar una aplicación educativa Android que contribuya al fortalecimiento de los aprendizajes de estudiantes de Tercero Básico, mediante actividades interactivas, seguimiento del progreso y acceso a contenidos complementarios.

### Objetivos específicos

- Proporcionar actividades educativas organizadas por asignatura.
- Incorporar tres niveles de dificultad para el aprendizaje progresivo.
- Implementar registro, inicio de sesión y gestión del perfil estudiantil.
- Registrar avances y resultados de los ejercicios.
- Permitir crear y administrar metas personales de estudio.
- Complementar las actividades mediante contenidos de Wikipedia.
- Implementar pruebas unitarias, instrumentadas y de integración.
- Generar una versión APK firmada para dispositivos Android compatibles.

## 3. Asignaturas y actividades

La aplicación incluye cuatro asignaturas:

| Asignatura | Contenido |
|---|---|
| 📖 Lenguaje y Comunicación | Comprensión, vocabulario y habilidades lingüísticas |
| 🔢 Matemática | Operaciones, cálculo y resolución de problemas |
| 🧪 Ciencias Naturales | Seres vivos, naturaleza y fenómenos científicos |
| 🌎 Historia y Geografía | Orientación, mapas, patrimonio y entorno |

### Organización de los ejercicios

El banco de preguntas contiene **60 ejercicios**, distribuidos de la siguiente manera:

| Dificultad | Ejercicios por asignatura |
|---|---:|
| Fácil ⭐ | 5 |
| Medio ⭐⭐ | 5 |
| Difícil ⭐⭐⭐ | 5 |
| **Total por asignatura** | **15** |

Las cuatro asignaturas suman 60 ejercicios.

Cada actividad incorpora una pregunta, alternativas de respuesta, identificación de la respuesta correcta y contenido de apoyo para el aprendizaje.

## 4. Funcionalidades implementadas

### 4.1. Bienvenida

- Pantalla inicial con la mascota educativa MPAZ.
- Diseño adaptable a distintos tamaños de pantalla.
- Navegación hacia el inicio de sesión.

### 4.2. Registro de estudiantes

- Ingreso de nombre completo.
- Validación de correo electrónico.
- Ingreso y confirmación de contraseña.
- Curso correspondiente a Tercero Básico.
- Validaciones de formularios.
- Integración con el servicio de registro de estudiantes.

### 4.3. Inicio de sesión

- Autenticación mediante correo y contraseña.
- Validación de campos obligatorios.
- Mensajes de error y estados de carga.
- Integración con el backend Spring Boot.
- Navegación hacia el perfil del estudiante.

### 4.4. Perfil del estudiante

- Visualización de nombre y curso.
- Fotografía de perfil desde la galería.
- Captura de fotografía mediante la cámara.
- Objetivo educativo personal.
- Progreso general de aprendizaje.
- Avance por asignatura.
- Resultados y rendimiento académico.
- Acceso a asignaturas, metas y modo estudio.
- Opción de cerrar sesión.

### 4.5. Actividades educativas

- Selección de las cuatro asignaturas.
- Selección del nivel de dificultad.
- Resolución de ejercicios.
- Retroalimentación educativa.
- Registro y consulta del progreso.
- Visualización de resultados.

### 4.6. Metas de estudio

- Crear metas educativas.
- Consultar las metas registradas.
- Editar su título y descripción.
- Marcar metas como completadas.
- Eliminar metas.
- Integración con operaciones CRUD mediante Spring Boot.

### 4.7. Modo estudio

- Pantalla y controles dedicados al estudio.
- Activación y desactivación del modo.
- Persistencia de preferencias con DataStore.
- Componentes visuales educativos reutilizables.

### 4.8. Contenido externo de Wikipedia

MPAZ PRO 3.º incorpora una integración con la API REST de Wikipedia en español.

Las actividades educativas contienen referencias a artículos relacionados con sus temas.

Esta integración permite consultar información complementaria y abrir el artículo correspondiente como fuente de apoyo.

Se implementaron componentes, repositorios y ViewModel reutilizables para utilizar esta funcionalidad en distintas asignaturas.

## 5. Tecnologías utilizadas

### Aplicación Android

| Tecnología | Uso |
|---|---|
| Kotlin | Lenguaje principal |
| Android Studio | Entorno de desarrollo |
| Jetpack Compose | Interfaces declarativas |
| Material Design 3 | Componentes visuales |
| Navigation Compose | Navegación |
| MVVM | Organización de la aplicación |
| ViewModel y StateFlow | Gestión del estado |
| Kotlin Coroutines | Operaciones asíncronas |
| Retrofit | Consumo de servicios REST |
| OkHttp | Cliente HTTP y gestión de cookies |
| Gson | Conversión de datos JSON |
| DataStore Preferences | Persistencia de preferencias |
| Activity Result API | Acceso a cámara y galería |
| FileProvider | Gestión segura de URI de archivos |
| Gradle | Compilación y dependencias |

### Backend e integración

- Java y Spring Boot.
- API REST para estudiantes, metas y progreso.
- PostgreSQL para persistencia remota.
- Wikipedia REST API para contenido educativo externo.

### Calidad y gestión

- JUnit.
- MockK.
- Robolectric.
- MockWebServer.
- AndroidX Test.
- Espresso.
- Jetpack Compose UI Test.
- JaCoCo.
- Git y GitHub.
- Jira.

## 6. Arquitectura de software

La aplicación utiliza una arquitectura basada en **Model-View-ViewModel (MVVM)**, junto con repositorios y servicios de comunicación.

### Capa de presentación

Contiene las pantallas construidas con Jetpack Compose, los componentes reutilizables y los elementos de navegación.

### Capa ViewModel

Gestiona los estados de interfaz, operaciones asíncronas y comunicación entre las pantallas y los repositorios.

### Capa Repository

Organiza el acceso a fuentes de información locales y remotas, incluyendo estudiantes, progreso, metas y contenido externo.

### Capa de datos

Contiene modelos, interfaces Retrofit, clientes HTTP y mecanismos de persistencia.

### Backend

Spring Boot expone los servicios REST y realiza las operaciones de persistencia con PostgreSQL.

**Flujo general:**

Interfaz Compose → ViewModel → Repository → Retrofit / OkHttp → Spring Boot → PostgreSQL.

La integración con Wikipedia utiliza un cliente Retrofit independiente.

## 7. Estructura del proyecto

Carpeta principal:

`app/src/main/java/com/example/appmpazpro3_grupo6/`

| Carpeta | Responsabilidad |
|---|---|
| `data/remote` | Clientes Retrofit, API y DTO |
| `model` | Modelos de datos |
| `navigation` | Rutas y navegación |
| `repository` | Acceso a datos locales y remotos |
| `ui/components` | Componentes Compose reutilizables |
| `ui/screens` | Pantallas de la aplicación |
| `ui/theme` | Colores y estilos |
| `ui/utils` | Utilidades nativas |
| `validation` | Validadores de formularios |
| `viewmodel` | Gestión de estados y lógica de presentación |

Las pruebas se encuentran en:

- `app/src/test/`: pruebas unitarias y de integración ejecutables en la JVM.
- `app/src/androidTest/`: pruebas instrumentadas en dispositivos Android o emuladores.

## 8. Comunicación con Spring Boot

La aplicación integra servicios remotos para autenticación, gestión de estudiantes, metas y seguimiento del progreso.

Entre sus endpoints se encuentran:

| Endpoint | Función |
|---|---|
| `GET /api/estado` | Consultar el estado del backend |
| `GET /api/metas` | Listar metas |
| `GET /api/metas/{id}` | Consultar una meta |
| `POST /api/metas` | Crear una meta |
| `PUT /api/metas/{id}` | Actualizar una meta |
| `DELETE /api/metas/{id}` | Eliminar una meta |

También existen servicios para estudiantes y progreso académico.

El backend utiliza PostgreSQL como sistema de almacenamiento.

### Configuración de conexión

La dirección del servidor se encuentra en:

`data/remote/ClienteEstudiantesMPAZ.kt`

La configuración actual utiliza `http://10.0.2.2:8080/`, correspondiente al acceso al servidor local desde el emulador Android.

Para utilizar las funcionalidades remotas en un teléfono independiente, se requiere configurar un backend accesible desde ese dispositivo.

**No se debe utilizar la configuración local del emulador como dirección de producción.**

Para un despliegue real se recomienda utilizar HTTPS, una configuración de red adecuada y medidas de seguridad para proteger las credenciales.

## 9. Requisitos de ejecución

### Requisitos Android

- Android 13 o superior.
- API mínima: 33.
- API objetivo: 37.
- Dispositivo Android compatible o emulador.

### Requisitos de desarrollo

- Android Studio.
- JDK compatible con el proyecto.
- Android SDK.
- Gradle Wrapper incluido en el repositorio.
- Conexión a Internet para dependencias y servicios externos.

### Requisitos para funcionalidades remotas

- Backend Spring Boot funcionando.
- PostgreSQL configurado.
- Acceso de red desde Android al backend.
- Conexión a Internet para consultar Wikipedia.

## 10. Instalación y ejecución desde Android Studio

1. Clonar el repositorio desde GitHub.
2. Abrir el proyecto en Android Studio.
3. Sincronizar las dependencias de Gradle.
4. Seleccionar un emulador o dispositivo Android compatible.
5. Configurar la dirección del backend según el entorno.
6. Ejecutar la aplicación mediante Run.

Repositorio:

https://github.com/Tiema2005/AppMPAZPRO3_Grupo6

### Compilación Debug

En PowerShell, desde la raíz del proyecto, ejecutar:

`.\gradlew.bat :app:assembleDebug`

### Compilación Release

`.\gradlew.bat :app:assembleRelease`

La generación de una APK release instalable requiere una firma digital válida.

## 11. Pruebas y calidad de software

Se implementaron pruebas automatizadas sobre validaciones, repositorios, ViewModel, integración con APIs y componentes de interfaz.

### Pruebas unitarias

Se realizaron pruebas sobre:

- Validación de formularios.
- Operaciones de repositorios.
- Estados de ViewModel.
- Gestión de estudiantes y metas.
- Progreso académico.
- Tratamiento de errores y excepciones.
- Integración de lectura con Wikipedia.

Comando:

`.\gradlew.bat :app:testDebugUnitTest`

Las ejecuciones finales registradas terminaron con `BUILD SUCCESSFUL`.

### Pruebas de Wikipedia

Se implementaron pruebas para comprobar las referencias a Wikipedia de las cuatro asignaturas y la reutilización de la integración.

Incluyen las clases `WikipediaBancoCompletoTest`, `WikipediaActividadesTest` y `LecturasCienciasReutilizacionTest`, entre otras.

Las comprobaciones del banco de 60 ejercicios finalizaron exitosamente durante el desarrollo.

### Pruebas instrumentadas

Se ejecutaron pruebas en un Motorola moto g34 5G con Android 15.

**Resultado documentado:**

| Indicador | Resultado |
|---|---:|
| Pruebas ejecutadas | 14 |
| Aprobadas | 14 |
| Fallidas | 0 |
| Omitidas | 0 |
| Porcentaje de éxito | 100 % |

Las pruebas incluyen validación de formularios, componentes de interfaz Compose y verificación del contexto Android.

Comando:

`.\gradlew.bat :app:connectedDebugAndroidTest`

La ejecución requiere un dispositivo físico o emulador disponible. Una ejecución posterior no pudo realizarse porque no había dispositivos conectados; esto no corresponde a un fallo de las pruebas.

### Cobertura

El proyecto incorpora JaCoCo y clases de pruebas destinadas a validar la lógica de repositorios, ViewModel y otros componentes.

La existencia de estas pruebas no equivale, por sí sola, a una medición global de cobertura. Debe consultarse el informe correspondiente para confirmar un porcentaje específico.

## 12. APK release firmada

Se generó la primera versión APK firmada de MPAZ PRO 3.º.

| Característica | Valor |
|---|---|
| Versión | 1.0 |
| `versionCode` | 1 |
| Tipo | APK Release |
| Archivo de respaldo | `MPAZ_PRO3_v1.0_firmada.apk` |
| Tamaño | 49.420.063 bytes |
| Algoritmo de firma | RSA |
| Longitud de clave | 2048 bits |
| Esquema válido | APK Signature Scheme V2 |
| Resultado | Firma verificada correctamente |

### Integridad SHA-256

`F4E95252FD5340ADA02EE8D9440217CD1608E6E668D82B3DF8CB41E4ED6CDCBD`

El hash SHA-256 del APK original coincide con el de su copia de respaldo.

### Seguridad de la firma

La clave privada `.jks` y sus contraseñas se mantienen fuera del repositorio.

La misma clave debe conservarse para firmar futuras actualizaciones de la aplicación, manteniendo un identificador de aplicación compatible y aumentando el `versionCode`.

**La APK no se incluye en este repositorio como archivo binario de entrega.** Se conserva de manera separada para su distribución y verificación.

## 13. Limitaciones conocidas

La versión 1.0 está firmada y verificada criptográficamente, pero presenta las siguientes limitaciones:

- La configuración actual del backend está dirigida al emulador Android.
- Las funciones remotas requieren un servidor Spring Boot accesible desde el dispositivo.
- El backend no está documentado como servicio público de producción.
- La instalación de la APK release firmada todavía requiere validación en un dispositivo físico compatible.
- No se documenta una función de recuperación de contraseñas.
- La disponibilidad del contenido de Wikipedia depende de la conexión y del servicio externo.

Por estas razones, la firma digital válida no debe interpretarse como una certificación de funcionamiento completo en cualquier dispositivo o red.

## 14. Gestión del proyecto y evidencias

El proyecto fue desarrollado de forma colaborativa, utilizando GitHub para control de versiones y Jira para planificación y seguimiento.

### Épicas principales

| Épica | Descripción |
|---|---|
| EP-09 | Microservicios y persistencia remota |
| EP-10 | Integración de Android con los microservicios |
| EP-11 | Integración de API externa |
| EP-12 | Pruebas automatizadas y cobertura |
| EP-13 | APK firmado, evidencias y defensa |

### Actividades de entrega

| Ticket Jira | Trabajo |
|---|---|
| LT-72 | Pruebas instrumentadas de interfaz |
| LT-77 | Correcciones finales de las pantallas e ícono |
| LT-76 | Generación y verificación del APK firmado |
| LT-75 | Actualización del README y recopilación de evidencias |

### Documentación complementaria

- `docs/LT72_pruebas_interfaz.md`: documentación de pruebas instrumentadas.
- `docs/LT76_apk_release.md`: generación y verificación de la APK firmada.
- Historial de commits y Pull Requests de GitHub.
- Capturas y resultados adjuntos a las actividades correspondientes de Jira.

Las evidencias incluyen pruebas de compilación, resultados de pruebas automatizadas, integración de ramas, firma digital y verificación de integridad de la APK.

## 15. Equipo de desarrollo

**Duoc UC — Grupo 6**

- Emanuel Toledo
- Felipe Escobar
- Ervin Millanguir

El proyecto se desarrolló mediante trabajo colaborativo, con distribución de actividades en Jira y registro de contribuciones a través de GitHub.

## 16. Estado del proyecto

MPAZ PRO 3.º dispone de una implementación Android con pantallas educativas, navegación, banco de ejercicios, seguimiento de progreso, metas, integración con Wikipedia y servicios backend.

Las principales modificaciones visuales y funcionales fueron integradas al repositorio, y se generó una APK release v1.0 con firma digital válida.

**Estado de entrega:** versión académica documentada, con pruebas automatizadas registradas y primera APK firmada.

**Validaciones futuras:** instalación de la APK release en dispositivo físico y configuración definitiva del backend para uso independiente del entorno local.

---

**MPAZ PRO 3.º — Aprender, practicar y crecer cada día. 🦉**
