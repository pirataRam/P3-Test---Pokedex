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

## 🧪 Pruebas Unitarias

El proyecto cuenta con cobertura completa de pruebas unitarias para todas las reglas de negocio de los casos de uso:
*   `GetPokemonListPagedUseCaseTest`
*   `GetPokemonDetailUseCaseTest`
*   `GetFavoriteListUseCaseTest`
*   `AddFavoriteUseCaseTest`
*   `RemoveFavoriteUseCaseTest`
*   `IsFavoriteUseCaseTest`
*   `CheckInternetConnectionUseCaseTest`
*   `PokemonListViewModelTest` (y componentes de paginado)
*   `PokemonDetailViewModelTest`

Para ejecutar las pruebas desde consola:
```bash
./gradlew testDebugUnitTest
```
