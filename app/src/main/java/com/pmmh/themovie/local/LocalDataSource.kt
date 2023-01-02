package com.pmmh.themovie.local

import com.pmmh.themovie.local.entities.LocalMovies

interface LocalDataSource {
    suspend fun insertMovies(Movies: List<LocalMovies>)
    suspend fun getMovies(type: Int): List<LocalMovies>
    suspend fun deleteMovies(type: Int)
}