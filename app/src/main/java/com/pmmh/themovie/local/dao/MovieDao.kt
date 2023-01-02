package com.pmmh.themovie.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pmmh.themovie.local.entities.LocalMovies
import com.pmmh.themovie.model.Result
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularMovies(Movies: List<LocalMovies>)

    @Query("SELECT * FROM movies WHERE movie_type LIKE :type")
    suspend fun getMovies(type:Int): List<LocalMovies>

    @Query("DELETE FROM movies WHERE movie_type LIKE :type")
    suspend fun deleteMovies(type:Int)

}