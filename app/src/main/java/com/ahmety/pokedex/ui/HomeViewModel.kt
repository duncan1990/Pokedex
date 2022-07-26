package com.ahmety.pokedex.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ahmety.pokedex.api.AppService
import com.ahmety.pokedex.datasource.MainPagingDataSource
import com.ahmety.pokedex.model.Result
import com.ahmety.pokedex.repository.HomeRepository
import com.ahmety.pokedex.util.Resource
import com.ahmety.pokedex.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
   private val repository: HomeRepository,
   //  @ApplicationContext private val context: Context
) : ViewModel() {
  //  val pokemonList: MutableLiveData<Resource<Result?>> = MutableLiveData()

 /*  fun getPokemon(offset: Int){
        pokemonList.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
            if (hasInternetConnection(context)) {
                val response = homeRepository.getPokemon(offset)
                pokemonList.postValue(Resource.Success(response.body()))
                 } else{
                    pokemonList.postValue(Resource.Error("No Internet Connection"))
                    }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> pokemonList.postValue(Resource.Error("Network Failure " +  ex.localizedMessage))
                    else -> pokemonList.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }*/

    val listData = Pager(PagingConfig(pageSize = 20)) {
        MainPagingDataSource(repository)

    }.flow.cachedIn(viewModelScope)


}