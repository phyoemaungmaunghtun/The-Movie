package com.pmmh.themovie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.repository.DataRepository
import com.pmmh.themovie.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataRepository: DataRepository,
) : ViewModel() {
    private val _upcomingMovies = MutableLiveData<Resource<Movie>>()
    val upcomingMovies: LiveData<Resource<Movie>>
        get() = _upcomingMovies

    private val _popularMovies = MutableLiveData<Resource<Movie>>()
    val popularMovies: LiveData<Resource<Movie>>
        get() = _popularMovies

    private val _topRateMovies = MutableLiveData<Resource<Movie>>()
    val topRateMovies: LiveData<Resource<Movie>>
        get() = _topRateMovies

    fun getUpcomingMovies() {
        _upcomingMovies.value = Resource.Loading()
        viewModelScope.launch {
            _upcomingMovies.value = dataRepository.getUpcomingMovies("",1)
        }
    }

     fun fetchPopularMovies(language: String,page : Int){
        _popularMovies.value = Resource.Loading()
        viewModelScope.launch {
            _popularMovies.value = dataRepository.getPopularMovies(language,page)
        }
    }

    fun fetchTopRateMovies(language: String,page : Int){
        _topRateMovies.value = Resource.Loading()
        viewModelScope.launch {
            _topRateMovies.value = dataRepository.getTopRateMovies(language,page)
        }
    }
}