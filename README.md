# P3 Test - Pokedex (Android Nativo)

Este proyecto es una aplicación nativa de Android para explorar el catálogo de Pokémon (Pokédex), desarrollada en Kotlin, Jetpack Compose y siguiendo los principios más estrictos de **Clean Architecture**.

---

## ⚡ Características Principales

1.  **Arquitectura Limpia (Clean Architecture)**: Estructurada en cuatro paquetes base (`core`, `data`, `domain`, `presentation`) para asegurar un desacoplamiento total, alta testabilidad y mantenimiento sencillo.
2.  **Scroll Infinito con Paging 3**: Carga fluida y automática de Pokémon en grilla interactiva paginada mediante el uso de la biblioteca oficial Jetpack Paging 3.
3.  **Búsqueda Inteligente (Smart Cache)**: Permite buscar Pokémon por número (ID) o nombre. Implementa una estrategia que verifica primero el caché local de Room (SQLite) antes de hacer peticiones a PokéAPI vía Retrofit, ahorrando ancho de banda y ofreciendo velocidad instantánea.
4.  **Mis Favoritos (Offline-first)**: Pestaña dedicada con NavBar para visualizar los Pokémon marcados como favoritos. Almacenados en una tabla aislada para máxima persistencia y accesibilidad sin conexión a internet.
5.  **Detalle Interactivo (Cries, Estrella y Toast)**:
    *   Reproducción automática del rugido de los Pokémon.
    *   Estrella interactiva para marcar/desmarcar favoritos.
    *   Toast descriptivo al agregar un favorito y diálogo de confirmación con opción "Sí" y "No" para prevenir remociones accidentales.
6.  **Inyección de Dependencias unificada**: Centralizada en el paquete `core` mediante Koin DI.

---

## 🧪 Pruebas Unitarias

El proyecto cuenta con cobertura completa de pruebas unitarias para todas las reglas de negocio de los casos de uso:
*   `GetPokemonListPagedUseCaseTest`
*   `GetPokemonDetailUseCaseTest`
*   `GetFavoriteListUseCaseTest`
*   `AddFavoriteUseCaseTest`
*   `RemoveFavoriteUseCaseTest`
*   `IsFavoriteUseCaseTest`
*   `PokemonListViewModelTest` (y componentes de paginado)

Para ejecutar las pruebas desde consola:
```bash
./gradlew testDebugUnitTest
```
