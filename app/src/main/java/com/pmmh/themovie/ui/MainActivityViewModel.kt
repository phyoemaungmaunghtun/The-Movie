package com.pmmh.themovie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.movieCredit.MovieCredit
import com.pmmh.themovie.model.movieDetails.MovieDetails
import com.pmmh.themovie.model.youtubeTrailer.MovieTrailer
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

    private val _movieDetail = MutableLiveData<Resource<MovieDetails>>()
    val movieDetail: LiveData<Resource<MovieDetails>>
        get() = _movieDetail

    private val _movieCredit = MutableLiveData<Resource<MovieCredit>>()
    val movieCredit: LiveData<Resource<MovieCredit>>
        get() = _movieCredit

    private val _movieTrailer = MutableLiveData<Resource<MovieTrailer>>()
    val movieTrailer: LiveData<Resource<MovieTrailer>>
        get() = _movieTrailer

    private val _searchMovie = MutableLiveData<Resource<Movie>>()
    val searchMovie: LiveData<Resource<Movie>>
        get() = _searchMovie

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

    fun fetchMovieDetails(movieId: String,language: String) {
        _movieDetail.value = Resource.Loading()
        viewModelScope.launch {
            _movieDetail.value = dataRepository.getMovieDetail(movieId,language)
        }
    }

    fun fetchMovieCredit(movieId: String,language: String) {
        _movieCredit.value = Resource.Loading()
        viewModelScope.launch {
            _movieCredit.value = dataRepository.getMovieCredit(movieId,language)
        }
    }

    fun fetchMovieTrailer(movieId: String, language: String) {
        _movieTrailer.value = Resource.Loading()
        viewModelScope.launch {
            _movieTrailer.value = dataRepository.getMovieTrailer(movieId,language)
        }
    }

    fun searchMovie(query: String) {
        _searchMovie.postValue(Resource.Loading())
        viewModelScope.launch {
            _searchMovie.postValue(dataRepository.searchMovie(query))
        }
    }
}