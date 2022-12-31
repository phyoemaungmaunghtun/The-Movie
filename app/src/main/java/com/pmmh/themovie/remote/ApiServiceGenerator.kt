package com.pmmh.themovie.remote

import com.pmmh.themovie.R
import com.pmmh.themovie.TheMovieApplication.Companion.context
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.repository.Resource
import com.pmmh.themovie.utilities.Network
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiServiceGenerator @Inject
constructor(private val retrofit: Retrofit) {

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    fun handleResponse(responseCall: Any?): Resource<Movie> {
        return when (responseCall) {
            is Movie -> {
                Resource.Success(data = responseCall)
            }
            else -> {
                Resource.DataError(errorMessage = responseCall as String)
            }
        }
    }

    suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!Network.isConnected(context)) {
            return context.getString(R.string.no_internet)
        }
        return try {
            val response = responseCall.invoke()
            if (response.isSuccessful) {
                response.body()
            } else {
                context.getString(R.string.network_error)
            }
        } catch (e: IOException) {
            context.getString(R.string.network_error)
        }
    }
}