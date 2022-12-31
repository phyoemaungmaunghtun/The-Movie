package com.pmmh.themovie.repository

import com.pmmh.themovie.model.Movie
import retrofit2.Response
import retrofit2.http.GET
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

}