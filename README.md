# CAPEBE - Aplicación Móvil (MVP)

Proyecto académico para la gestión y registro de vuelos recreativos del Club Aéreo del Personal de BancoEstado (CAPEBE). 

Este repositorio contiene la primera etapa del Producto Mínimo Viable (MVP), enfocado exclusivamente en el flujo y experiencia del usuario con rol de **Invitado** (Socios y beneficiarios de Bienestar).

## Descripción del Proyecto

El objetivo de esta etapa es digitalizar el proceso de inscripción y control de los invitados a los vuelos recreativos en las bases de Tobalaba y Talcahuano. La aplicación permite a los usuarios registrarse, autenticarse, revisar las jornadas de vuelo disponibles y gestionar su perfil, eliminando la dependencia de planillas impresas y correos manuales.

## Alcance Actual (Rol Invitado)

El desarrollo actual cubre las siguientes pantallas y flujos:
* **Inicio (Home):** Vista principal con el estado de las próximas jornadas de vuelo.
* **Registro:** Creación de cuenta capturando datos esenciales (RUT, Nombre, Correo).
* **Login:** Autenticación de usuarios existentes.
* **Actividades:** Visualización del historial y estado de jornadas (abiertas, cerradas, finalizadas).
* **Perfil:** Gestión de la información de la cuenta del usuario.

## Características Técnicas Especiales

Durante el desarrollo de este MVP, se implementaron integraciones directas con el hardware del dispositivo Android para mejorar la experiencia del usuario y la seguridad:

* **Autenticación Biométrica:** Implementación de `BiometricPrompt` para permitir el inicio de sesión seguro y rápido utilizando la huella dactilar del dispositivo.
* **Retroalimentación Háptica:** Uso de `VibratorManager` para emitir respuestas táctiles en interacciones clave de la interfaz, mejorando la respuesta del sistema ante las acciones del usuario.

## Arquitectura y Tecnologías

El proyecto está desarrollado bajo el patrón de arquitectura **MVVM (Model-View-ViewModel)**, asegurando la separación de responsabilidades:

* **Lenguaje:** Kotlin
* **Interfaz de Usuario:** Jetpack Compose (Material Design 3)
* **Arquitectura:** MVVM
* **Persistencia Local:** Room / SQLite (Garantiza el funcionamiento offline en la pista de vuelo)
* **Control de Versiones:** Git y GitHub

## Estructura de Paquetes Principal

* `data.database` / `data.local`: Configuración de Room, Entidades y DAOs.
* `model`: Clases de datos para la lógica de negocio.
* `ui.screen` / `ui.components`: Pantallas e interfaces construidas con Jetpack Compose.
* `ui.viewmodel`: Gestión de estados de la UI y lógica de presentación.
* `ui.navigation`: Enrutamiento y navegación entre pantallas.

## Autores

* Nombre: Matias Fuentes
* Asignatura: Desarrollo de Aplicaciones Móviles
* Institución: Duoc UC
