package com.pmmh.themovie.repository

import com.pmmh.themovie.remote.RemoteData
import com.pmmh.themovie.remote.RemoteDataSource
import javax.inject.Inject

class DataRepository @Inject constructor(private val remoteRepository: RemoteData) :
    RemoteDataSource {
    /**
     * Network request by repository pattern
     */
    override suspend fun getUpcomingMovies(language: String, page: Int) =
        remoteRepository.getUpcomingMovies(language, page)

    override suspend fun getPopularMovies(language: String, page: Int) =
        remoteRepository.getPopularMovies(language, page)

    override suspend fun getTopRateMovies(language: String, page: Int) =
        remoteRepository.getTopRateMovies(language, page)

    override suspend fun getMovieDetail(movieId: String, language: String) =
        remoteRepository.getMovieDetail(movieId, language)

    override suspend fun getMovieCredit(movieId: String, language: String) =
        remoteRepository.getMovieCredit(movieId, language)

    override suspend fun getMovieTrailer(
        language: String,
        movieId: String
    ) = remoteRepository.getMovieTrailer(movieId, language)

}