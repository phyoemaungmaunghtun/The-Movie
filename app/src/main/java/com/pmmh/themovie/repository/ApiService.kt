package com.pmmh.themovie.repository

import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.movieCredit.MovieCredit
import com.pmmh.themovie.model.movieDetails.MovieDetails
import com.pmmh.themovie.model.youtubeTrailer.MovieTrailer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "86ab73a94cd4cfeaedc37fc0e2c0ed1a"

interface ApiService {
    @GET("3/movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcomingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("3/movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("3/movie/top_rated?api_key=$API_KEY")
    suspend fun getTopRateMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Movie>

    @GET("3/movie/{movieId}?api_key=$API_KEY")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: String,
        @Query("language") language: String
    ): Response<MovieDetails>

    @GET("3/movie/{movieId}/credits?api_key=$API_KEY")
    suspend fun getMovieCredit(
        @Path("movieId") movieId: String,
        @Query("language") language: String
    ): Response<MovieCredit>

    @GET("3/movie/{movieId}/videos?api_key=$API_KEY")
    suspend fun getMovieTrailer(
        @Path("movieId") movieId: String,
        @Query("language") language: String
    ): Response<MovieTrailer>

}