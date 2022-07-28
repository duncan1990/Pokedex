package com.ahmety.pokedex.repository

import com.ahmety.pokedex.api.AppService
import com.ahmety.pokedex.model.PokemonDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val appService: AppService
) {
    suspend fun getPokemonDetail(name: String): Response<PokemonDetail> = withContext(
        Dispatchers.IO
    ) {
        val pokemonDetail = appService.getPokemonDetail(name = name)
        pokemonDetail
    }
}