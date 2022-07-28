package com.ahmety.pokedex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ahmety.pokedex.datasource.MainPagingDataSource
import com.ahmety.pokedex.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    // get data from request with paging3 lib
    val listData = Pager(PagingConfig(pageSize = 20)) {
        MainPagingDataSource(repository)
    }.flow.cachedIn(viewModelScope)

}