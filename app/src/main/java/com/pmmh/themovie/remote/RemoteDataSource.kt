package com.pmmh.themovie.remote

import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.repository.Resource

internal interface RemoteDataSource {

    suspend fun getUpcomingMovies(language: String, page: Int): Resource<Movie>
    suspend fun getPopularMovies(language: String, page: Int): Resource<Movie>
    suspend fun getTopRateMovies(language: String, page: Int): Resource<Movie>

}