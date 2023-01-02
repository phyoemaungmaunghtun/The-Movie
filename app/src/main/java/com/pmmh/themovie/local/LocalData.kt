package com.pmmh.themovie.local

import com.pmmh.themovie.TheMovieApplication.Companion.context
import com.pmmh.themovie.local.entities.LocalMovies
import javax.inject.Inject

class LocalData @Inject constructor() : LocalDataSource {
    override suspend fun insertMovies(movies: List<LocalMovies>) {
        AppDatabase.getDatabase(context).movieDao().insertPopularMovies(movies)
    }

    override suspend fun getMovies(type: Int): List<LocalMovies> {
        return AppDatabase.getDatabase(context).movieDao().getMovies(type)
    }

    override suspend fun deleteMovies(type: Int) {
        AppDatabase.getDatabase(context).movieDao().deleteMovies(type)
    }
}