package com.example.p3test_pokedex.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Placeholder interface representing a generic data repository.
 * Included for testing or future extension purposes.
 */
interface DataRepository {
  /**
   * Emits a flow of lists of strings.
   */
  val data: Flow<List<String>>
}

/**
 * Default implementation of [DataRepository] that emits a mock list of strings.
 */
class DefaultDataRepository : DataRepository {
  /**
   * Flow emitting a static list containing a single element "Android".
   */
  override val data: Flow<List<String>> = flow { emit(listOf("Android")) }
}
