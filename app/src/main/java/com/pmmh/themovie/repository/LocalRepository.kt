package com.pmmh.themovie.repository

import com.pmmh.themovie.local.LocalData
import com.pmmh.themovie.local.LocalDataSource
import com.pmmh.themovie.local.entities.LocalMovies
import javax.inject.Inject
import com.pmmh.themovie.model.Result

class LocalRepository @Inject constructor(private val localData: LocalData) : LocalDataSource {
    override suspend fun insertMovies(movies: List<LocalMovies>) =
        localData.insertMovies(movies)

    override suspend fun getMovies(type: Int): List<LocalMovies> = localData.getMovies(type)

    override suspend fun deleteMovies(type: Int) = localData.deleteMovies(type)
}