package com.pmmh.themovie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmmh.themovie.local.entities.LocalMovies
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.Result
import com.pmmh.themovie.model.movieCredit.MovieCredit
import com.pmmh.themovie.model.movieDetails.MovieDetails
import com.pmmh.themovie.model.youtubeTrailer.MovieTrailer
import com.pmmh.themovie.repository.DataRepository
import com.pmmh.themovie.repository.LocalRepository
import com.pmmh.themovie.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataRepository: DataRepository, private val localRepository: LocalRepository
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

    private val _localUpComingMovies = MutableLiveData<List<Result>>()
    val localUpComingMovies: LiveData<List<Result>>
        get() = _localUpComingMovies

    private val _localPopularMovies = MutableLiveData<List<Result>>()
    val localPopularMovies: LiveData<List<Result>>
        get() = _localPopularMovies

    private val _localTopRateMovies = MutableLiveData<List<Result>>()
    val localTopRateMovies: LiveData<List<Result>>
        get() = _localTopRateMovies

    fun getUpcomingMovies() {
        _upcomingMovies.value = Resource.Loading()
        viewModelScope.launch {
            _upcomingMovies.value = dataRepository.getUpcomingMovies("", 1)
        }
    }

    fun fetchPopularMovies(language: String, page: Int) {
        _popularMovies.value = Resource.Loading()
        viewModelScope.launch {
            _popularMovies.value = dataRepository.getPopularMovies(language, page)
        }
    }

    fun fetchTopRateMovies(language: String, page: Int) {
        _topRateMovies.value = Resource.Loading()
        viewModelScope.launch {
            _topRateMovies.value = dataRepository.getTopRateMovies(language, page)
        }
    }

    fun fetchMovieDetails(movieId: String, language: String) {
        _movieDetail.value = Resource.Loading()
        viewModelScope.launch {
            _movieDetail.value = dataRepository.getMovieDetail(movieId, language)
        }
    }

    fun fetchMovieCredit(movieId: String, language: String) {
        _movieCredit.value = Resource.Loading()
        viewModelScope.launch {
            _movieCredit.value = dataRepository.getMovieCredit(movieId, language)
        }
    }

    fun fetchMovieTrailer(movieId: String, language: String) {
        _movieTrailer.value = Resource.Loading()
        viewModelScope.launch {
            _movieTrailer.value = dataRepository.getMovieTrailer(movieId, language)
        }
    }

    fun cacheMovies(type: Int, results: List<Result>) {
        viewModelScope.launch {
            val list = localRepository.getMovies(type)
            if (list.isNotEmpty()) {
                localRepository.deleteMovies(type)
            }
            when (type) {
                1 -> results.forEach { movie -> movie.movieType = 1 }
                2 -> results.forEach { movie -> movie.movieType = 2 }
                3 -> results.forEach { movie -> movie.movieType = 3 }
            }
            localRepository.insertMovies(convertOnlineToLocal(results))
        }
    }

    fun getLocalMovie(type: Int) {
        viewModelScope.launch {
            val localUpcoming = localRepository.getMovies(type)
            when (type) {
                1 -> _localUpComingMovies.value = convertLocalToOnline(localUpcoming)
                2 -> _localPopularMovies.value = convertLocalToOnline(localUpcoming)
                3 -> _localTopRateMovies.value = convertLocalToOnline(localUpcoming)
            }
        }
    }

    private fun convertOnlineToLocal(results: List<Result>): List<LocalMovies> {
        return results.map { result -> LocalMovies(result) }
    }

    private fun convertLocalToOnline(movies: List<LocalMovies>): List<Result> {
        return movies.map { movie -> Result(movie) }
    }

}