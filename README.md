# P3 Test - Pokedex (Android Nativo)

Este proyecto es una aplicación nativa de Android para explorar el catálogo de Pokémon (Pokédex), desarrollada en Kotlin, Jetpack Compose y siguiendo los principios más estrictos de **Clean Architecture**.

---

## ⚡ Características Principales

1.  **Arquitectura Limpia (Clean Architecture)**: Estructurada en cuatro paquetes base (`core`, `data`, `domain`, `presentation`) para asegurar un desacoplamiento total, alta testabilidad y mantenimiento sencillo.
2.  **Interfaz 100% Declarativa (Single Activity & Zero XML Views/Fragments)**: 
    *   Migración completa de la navegación basada en fragmentos y gráficos XML (`nav_graph.xml`, `activity_main.xml`) hacia **Jetpack Compose Navigation** nativo (`NavHost`, `composable`).
    *   La aplicación se despliega completamente a través de una única `MainActivity` que extiende de `ComponentActivity`, optimizando la jerarquía de vistas y el rendimiento general.
3.  **Scroll Infinito con Paging 3**: Carga fluida y automática de Pokémon en grilla interactiva paginada mediante el uso de la biblioteca oficial Jetpack Paging 3.
4.  **Búsqueda Inteligente (Smart Cache)**: Permite buscar Pokémon por número (ID) o nombre. Implementa una estrategia que verifica primero el caché local de Room (SQLite) antes de hacer peticiones a PokéAPI vía Retrofit, ofreciendo velocidad instantánea.
5.  **Mis Favoritos (Offline-first)**: Pestaña dedicada con barra de navegación inferior para visualizar los Pokémon marcados como favoritos. Almacenados en una tabla aislada para máxima persistencia y accesibilidad sin conexión a internet.
6.  **Persistencia del Estado del Tab (Anti-Rotación)**: Conservación impecable de la pantalla/pestaña seleccionada (Pokédex o Favoritos) mediante el uso de `rememberSaveable { mutableStateOf(MainTab.Pokedex) }` que sobrevive a los cambios de configuración como la rotación del dispositivo.
7.  **Detalle Interactivo (Cries, Estrella y Toast)**:
    *   Reproducción automática de los rugidos de los Pokémon cargados/deslizados.
    *   Estrella interactiva para marcar/desmarcar favoritos.
    *   Toast descriptivo al agregar un favorito y diálogo de confirmación con opción "Sí" y "No" para prevenir remociones accidentales.
8.  **Soporte Multi-idioma de Localización (Español / Inglés)**: 
    *   Extracción del 100% de las cadenas de texto harcodeadas en pantallas a recursos de strings estándar (`strings.xml`).
    *   Soporte para **Español** (`values-es/strings.xml`) e **Inglés** por defecto (`values/strings.xml`), que se adaptan en tiempo real según el idioma preferido en el sistema operativo del teléfono.
9.  **Inyección de Dependencias unificada**: Centralizada en el paquete `core` mediante Koin DI y adaptada en Compose mediante `koinViewModel()`.
10. **Seguridad de Red Declarada**: Corrección de fallos relacionados con el monitoreo de conectividad de red mediante la inclusión formal del permiso `android.permission.ACCESS_NETWORK_STATE` en el manifiesto.

---

## 🏗️ Arquitectura Propuesta y Decisiones Técnicas

La aplicación sigue los principios de **Clean Architecture** estructurada en capas independientes con flujo de dependencia unidireccional (capas externas conocen a las internas, pero no al revés):

*   **Capas del Proyecto**:
    *   `core`: Contiene utilidades transversales como la configuración del módulo global de Inyección de Dependencias (Koin) y constantes comunes.
    *   `domain`: La capa más interna y pura. Contiene los modelos de dominio (`Pokemon`, `PokemonDetail`, `PokemonStat`), contratos de repositorios (interfaces) e implementaciones de Casos de Uso (`UseCases`) de negocio independientes de la plataforma.
    *   `data`: Implementación del acceso a datos. Gestiona la comunicación con la API remota (`PokeApiService` vía Retrofit) y el almacenamiento local persistente (`PokemonDao` vía Room). Resuelve la lógica de "caché inteligente" decidiendo de dónde extraer la información según la conectividad y disponibilidad.
    *   `presentation`: Capa visual reactiva construida en Jetpack Compose. Utiliza **ViewModels** para mantener y emitir de forma segura el estado (`StateFlow`) que observan las vistas declarativas (Composables).

*   **Decisiones Técnicas Clave**:
    *   **Single Activity Architecture**: Eliminar fragmentos y vistas XML reduce la sobrecarga de memoria del ciclo de vida tradicional y simplifica la navegación mediante Compose Navigation nativo.
    *   **Offline-First para Favoritos**: Los Pokémon marcados como favoritos se almacenan localmente en Room. Esto permite que el usuario los consulte incluso sin conexión de datos.
    *   **Paging 3 con Room**: Se utiliza `PagingSource` de forma limpia conectando el flujo de paginación del repositorio directamente a Compose, permitiendo scroll infinito fluido con bajo consumo de memoria.

---

## 🛠️ Tecnologías y Librerías Utilizadas

A continuación se listan las principales librerías implementadas y su justificación técnica:

| Librería | Propósito / Justificación |
| :--- | :--- |
| **Jetpack Compose** | Framework moderno y declarativo para construir UIs nativas, rápidas y dinámicas en Android con menos código. |
| **Compose Navigation** | Gestiona la navegación de la aplicación de forma declarativa, eliminando por completo la complejidad de fragmentos y XMLs. |
| **Koin** | Framework pragmático de inyección de dependencias para Kotlin. Es extremadamente liviano, fácil de configurar y se integra de forma nativa con ViewModels de Jetpack Compose. |
| **Room Database** | Biblioteca de persistencia sobre SQLite oficial de Google. Permite mapeo de objetos robusto, validación de consultas en tiempo de compilación y soporte completo para corrutinas (Ktx). |
| **Retrofit + OkHttp** | Cliente HTTP estándar de la industria. Permite declarar endpoints de PokéAPI de manera tipada y configurar interceptores de logging de red sencillos. |
| **Coil** | Cargador de imágenes moderno de alto rendimiento para Android respaldado por corrutinas de Kotlin. Configurado globalmente en la app con caché persistente en disco (de hasta 300 MiB) para visualización offline de Pokémon. |
| **Paging 3 (Paging-Compose)** | Facilita la carga incremental de listas de datos (scroll infinito), gestionando automáticamente estados de carga, reintentos y liberando memoria eficientemente. |
| **KotlinX Coroutines & Flow** | Gestor de hilos asíncronos y flujos reactivos de datos para comunicar capas del backend de la app hacia la UI de manera limpia. |

---

## 🚀 Instalación y Ejecución del Proyecto

Sigue estos pasos sencillos para instalar y correr el proyecto localmente:

### 📋 Requisitos Previos
*   **Android Studio** Ladybug (2024.2.1) o superior.
*   **JDK 17** configurado en tu entorno de desarrollo.
*   Dispositivo físico Android o Emulador con **Android 7.0 (API 24)** o superior.

### 📥 Paso 1: Clonar el Repositorio
```bash
git clone https://github.com/pirataRam/P3-Test---Pokedex.git
cd P3-Test---Pokedex
```

### ⚙️ Paso 2: Importar el Proyecto
1. Abre **Android Studio**.
2. Selecciona **File > Open** y busca el directorio clonado.
3. Espera que Gradle finalice la sincronización e instalación automática de dependencias especificadas en `libs.versions.toml`.

### 📱 Paso 3: Compilar y Ejecutar
1. Conecta tu dispositivo o inicia el emulador.
2. Haz clic en el botón verde **Run (Play)** en la barra de herramientas superior de Android Studio (o usa la combinación `Shift + F10`).
3. También puedes construir el APK de depuración desde consola:
   ```bash
   ./gradlew assembleDebug
   ```
   El APK resultante estará disponible en `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🧪 Pruebas Unitarias

El proyecto cuenta con cobertura completa de pruebas unitarias para asegurar la estabilidad de las reglas de negocio en la capa de dominio y presentación:

*   **Pruebas de Casos de Uso (`domain/usecase`)**:
    *   `GetPokemonListPagedUseCaseTest`
    *   `GetPokemonDetailUseCaseTest`
    *   `GetFavoriteListUseCaseTest`
    *   `AddFavoriteUseCaseTest`
    *   `RemoveFavoriteUseCaseTest`
    *   `IsFavoriteUseCaseTest`
    *   `CheckInternetConnectionUseCaseTest`
*   **Pruebas de ViewModel (`presentation`)**:
    *   `PokemonListViewModelTest` (pruebas de paginado, filtrado y estados de conexión)
    *   `PokemonDetailViewModelTest` (pruebas de UIState y flujos de inserción/remoción de favoritos)

Para ejecutar la suite de pruebas unitarias completa desde la terminal de consola:
```bash
./gradlew testDebugUnitTest
```

---

## 📸 Evidencia de Funcionamiento

*(Opcional recomendado)* Puedes añadir capturas de pantalla o un enlace a un video demostrativo aquí para validar visualmente el comportamiento de la Pokédex:

*   **Pestaña Pokédex (Scroll Infinito y Búsqueda)**:
    *   `[Inserta aquí tu captura: listado_pokedex.png]`
*   **Pestaña Mis Favoritos**:
    *   `[Inserta aquí tu captura: listado_favoritos.png]`
*   **Detalle Interactivos**:
    *   `[Inserta aquí tu captura: detalle_pokemon.png]`

---

## 📌 Pendientes, Trade-offs y Mejoras Futuras

Al diseñar la solución, se tomaron ciertas decisiones de diseño (trade-offs) y se identificaron áreas de mejora continua:

*   **Trade-off de Paginación**:
    *   *Decisión*: Se utiliza paginación basada en compensación de desplazamiento (`Offset`/`Limit`) provisto directamente por el PokéAPI en lugar de paginación basada en claves/cursores. Aunque cursor es más robusto para listas altamente dinámicas que cambian en tiempo real, `Offset` es el estándar nativo para PokéAPI y es sumamente rápido para catálogos estáticos de Pokémon.
*   **Búsqueda Local Offline Completa**:
    *   *Pendiente / Mejora*: Actualmente, la búsqueda inteligente primero valida en Room. Sin embargo, dado que PokéAPI provee más de 1025 registros, una mejora futura sería realizar una sincronización inicial silenciosa (ingesta en segundo plano de IDs y nombres) en el primer inicio de la app para que la búsqueda por texto funcione de manera 100% offline incluso si el elemento no se ha cargado previamente en el scroll de la lista.
*   **Animaciones de Transición en HorizontalPager**:
    *   *Mejora*: Implementar transiciones de animación más sofisticadas y personalizadas en el carrusel de detalles al deslizar entre Pokémon izquierdo/derecho.
*   **Preferencias de Idioma Integradas (Android 13+)**:
    *   *Mejora*: Añadir compatibilidad con el selector de idioma por aplicación nativo de Android 13 para cambiar entre español e inglés de forma interna sin requerir cambiar el idioma del sistema global.
