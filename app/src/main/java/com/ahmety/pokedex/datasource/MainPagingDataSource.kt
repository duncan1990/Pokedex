package com.ahmety.pokedex.datasource

import androidx.paging.PagingSource
import com.ahmety.pokedex.model.ErrorResponse
import com.ahmety.pokedex.model.Pokemon
import com.ahmety.pokedex.model.Result
import com.ahmety.pokedex.repository.HomeRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class MainPagingDataSource(
    private val homeRepository: HomeRepository
) : PagingSource<Int, Pokemon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            var count: Long = 0
            val nextOffsetNumber = params.key ?: 0
            val response = homeRepository.getPokemon(nextOffsetNumber)
            if (response.isSuccessful) {
                val data = response.body()?.results
                response.body()?.count?.let { count = it }
                LoadResult.Page(
                    data = data.orEmpty(),
                    prevKey = if (nextOffsetNumber > 0) nextOffsetNumber - 20 else null,
                    nextKey = if (nextOffsetNumber < count) nextOffsetNumber + 20 else null
                )
            } else {
                getError(response)
                LoadResult.Error(java.lang.Exception())
            }
        } catch (retryAbleError: Exception) {
                LoadResult.Error(retryAbleError)
        }
    }

    private fun getError(response: Response<Result>) {
        val type = object : TypeToken<ErrorResponse>() {}.type
        response.errorBody()?.let { errorBody ->
            errorResponse = Gson().fromJson(errorBody.charStream(), type)
        }
    }

    companion object {
        var errorResponse: ErrorResponse? = null
    }
}