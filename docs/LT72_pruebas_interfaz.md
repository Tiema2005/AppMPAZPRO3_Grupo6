# LT-72 - Pruebas automatizadas de interfaz

## Proyecto
MPAZ PRO 3 - Grupo 6

## Objetivo
Documentar la ejecución de pruebas instrumentadas
y las validaciones de interfaz realizadas en Android.

## Dispositivo
Motorola moto g34 5G
Android 15

## Resultados
Fecha: 23-11-2026

- Pruebas ejecutadas: 14
- Pruebas aprobadas: 14
- Pruebas fallidas: 0
- Pruebas omitidas: 0
- Tasa de éxito: 100%

## Pruebas incluidas
- 6 pruebas Compose del componente Wikipedia.
- 7 pruebas instrumentadas de validación.
- 1 prueba del contexto Android.

## Comando de ejecución
.\gradlew.bat :app:connectedDebugAndroidTest --rerun-tasks

## Evidencia
Informe Gradle generado correctamente.

Ruta local:
app/build/reports/androidTests/connected/debug/index.html

La captura del informe se adjunta al ticket LT-72 de Jira.

## Pruebas manuales
Se realizaron comprobaciones adicionales de navegación,
inicio de sesión, perfil, asignaturas y otras funciones
desde un dispositivo Android físico.

Estas comprobaciones manuales no se contabilizan
como pruebas automatizadas.

## Limitaciones
Los recorridos completos de autenticación, metas y
progreso no están cubiertos íntegramente por las
14 pruebas instrumentadas.

## Resultado
BUILD SUCCESSFUL.
Las 14 pruebas instrumentadas finalizaron sin fallos.
