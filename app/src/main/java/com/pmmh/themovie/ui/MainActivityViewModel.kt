package com.pmmh.themovie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.repository.DataRepository
import com.pmmh.themovie.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataRepository: DataRepository,
) : ViewModel() {
    private val _upcomingMovies = MutableLiveData<Resource<Movie>>()
    val upcomingMovies: LiveData<Resource<Movie>>
        get() = _upcomingMovies

    fun getUpcomingMovies() {
        _upcomingMovies.value = Resource.Loading()
        viewModelScope.launch {
            _upcomingMovies.value = dataRepository.getUpcomingMovies("",1)
        }
    }
}