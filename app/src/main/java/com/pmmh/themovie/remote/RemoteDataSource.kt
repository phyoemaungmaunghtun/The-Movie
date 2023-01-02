package com.pmmh.themovie.remote

import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.movieCredit.MovieCredit
import com.pmmh.themovie.model.movieDetails.MovieDetails
import com.pmmh.themovie.model.youtubeTrailer.MovieTrailer
import com.pmmh.themovie.repository.Resource

internal interface RemoteDataSource {

    suspend fun getUpcomingMovies(language: String, page: Int): Resource<Movie>
    suspend fun getPopularMovies(language: String, page: Int): Resource<Movie>
    suspend fun getTopRateMovies(language: String, page: Int): Resource<Movie>
    suspend fun getMovieDetail(language: String, movieId: String): Resource<MovieDetails>
    suspend fun getMovieCredit(language: String, movieId: String): Resource<MovieCredit>
    suspend fun getMovieTrailer(language: String, movieId: String): Resource<MovieTrailer>

}