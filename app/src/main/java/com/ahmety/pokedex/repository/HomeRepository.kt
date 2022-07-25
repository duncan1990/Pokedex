package com.ahmety.pokedex.repository

import com.ahmety.pokedex.api.AppService
import com.ahmety.pokedex.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val appService: AppService
) {
    suspend fun getPokemon(offset: Int, limit: Int): Response<Result> = withContext(
        Dispatchers.IO
    ) {
        val pokemon = appService.getPokemon(offset = offset, limit = limit)
        pokemon

    }
}