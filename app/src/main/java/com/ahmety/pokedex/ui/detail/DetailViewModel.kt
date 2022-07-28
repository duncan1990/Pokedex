package com.ahmety.pokedex.ui.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmety.pokedex.model.PokemonDetail
import com.ahmety.pokedex.repository.DetailRepository
import com.ahmety.pokedex.util.Resource
import com.ahmety.pokedex.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    // define repository value
    private val repository: DetailRepository,
    private val application: Application
) : ViewModel() {

    // define list variable
    val pokemonList: MutableLiveData<Resource<PokemonDetail?>> = MutableLiveData()

    // retrofit request method
    fun getPokemonDetail(pokemonName: String) {
        pokemonList.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection(application.applicationContext)) {
                    val response = repository.getPokemonDetail(pokemonName)
                    pokemonList.postValue(Resource.Success(response.body()))
                } else {
                    pokemonList.postValue(Resource.Error("No Internet Connection"))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> pokemonList.postValue(Resource.Error("Network Failure " + ex.localizedMessage))
                    else -> pokemonList.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }
}