package com.ahmety.pokedex.api

import com.ahmety.pokedex.model.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    companion object {
        const val ENDPOINT = "https://pokeapi.co/api/v2/"
    }

    @GET("pokemon")
    suspend fun getPokemon(@Query("offset") offset: Int, @Query("limit") limit: Int): Response<Result>

    @GET("")
    suspend fun getPokemonDetail(@Path("pokemonId") pokemonId: Int): Response<Result>
}