package com.pmmh.themovie.remote

import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.UserListResponseModel
import com.pmmh.themovie.model.movieCredit.MovieCredit
import com.pmmh.themovie.model.movieDetails.MovieDetails
import com.pmmh.themovie.model.youtubeTrailer.MovieTrailer
import com.pmmh.themovie.repository.ApiService
import com.pmmh.themovie.repository.Resource
import javax.inject.Inject

class RemoteData @Inject
constructor(private val serviceGenerator: ApiServiceGenerator) : RemoteDataSource {

    private val service = serviceGenerator.createService(ApiService::class.java)

    override suspend fun getUpcomingMovies(language:String,page:Int
    ): Resource<Movie> {
        return serviceGenerator.handleResponse(serviceGenerator.processCall {
            service.getUpcomingMovies(language,page)
        }) as Resource<Movie>
    }

    override suspend fun getPopularMovies(language: String, page: Int): Resource<Movie> {
        return serviceGenerator.handleResponse(serviceGenerator.processCall {
            service.getPopularMovies(language,page)
        }) as Resource<Movie>
    }

    override suspend fun getTopRateMovies(language: String, page: Int): Resource<Movie> {
        return serviceGenerator.handleResponse(serviceGenerator.processCall {
            service.getTopRateMovies(language,page)
        }) as Resource<Movie>
    }

    override suspend fun getMovieDetail( movieId: String,language: String): Resource<MovieDetails> {
        return serviceGenerator.handleResponse(serviceGenerator.processCall {
            service.getMovieDetails(movieId,language)
        }) as Resource<MovieDetails>
    }
    override suspend fun getMovieCredit(movieId: String,language: String): Resource<MovieCredit> {
        return serviceGenerator.handleResponse(serviceGenerator.processCall {
            service.getMovieCredit(movieId,language)
        }) as Resource<MovieCredit>
    }

    override suspend fun getMovieTrailer(
        language: String,
        movieId: String
    ): Resource<MovieTrailer> {
        return serviceGenerator.handleResponse(serviceGenerator.processCall {
            service.getMovieTrailer(movieId,language)
        }) as Resource<MovieTrailer>
    }
}