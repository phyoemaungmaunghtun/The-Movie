package com.pmmh.themovie.remote

import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.UserListResponseModel
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
        })
    }

    override suspend fun getPopularMovies(language: String, page: Int): Resource<Movie> {
        return serviceGenerator.handleResponse(serviceGenerator.processCall {
            service.getPopularMovies(language,page)
        })
    }
}