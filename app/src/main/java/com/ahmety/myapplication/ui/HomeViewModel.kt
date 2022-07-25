package com.ahmety.myapplication.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmety.myapplication.model.Result
import com.ahmety.myapplication.repository.HomeRepository
import com.ahmety.myapplication.util.Resource
import com.ahmety.myapplication.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
   private val homeRepository: HomeRepository,
   @ApplicationContext private val context: Context
) : ViewModel() {
    val pokemonList: MutableLiveData<Resource<Result?>> = MutableLiveData()

   fun getPokemon(offset: Int, limit: Int){
        pokemonList.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
            if (hasInternetConnection(context)) {
                val response = homeRepository.getPokemon(offset,limit)
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
    }

}