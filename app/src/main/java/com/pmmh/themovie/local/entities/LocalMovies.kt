package com.pmmh.themovie.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pmmh.themovie.local.IntTypeConverter

@Entity(tableName = "movies")
data class LocalMovies(
    @ColumnInfo(name = "movie_type") val movieType: Int?,
    @ColumnInfo(name = "adult") val adult: Boolean?,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "genre_ids")@field:TypeConverters(IntTypeConverter::class)
    val genreIds: List<Int>?,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "original_language") val originalLanguage: String?,
    @ColumnInfo(name = "original_title") val originalTitle: String?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "popularity") val popularity: Double?,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "video") val video: Boolean?,
    @ColumnInfo(name = "vote_average") val voteAverage: Double?,
    @ColumnInfo(name = "vote_count") val voteCount: Int?
){ constructor(result: com.pmmh.themovie.model.Result):this(
    result.movieType,
    result.adult,
    result.backdropPath,
    result.genreIds,
    result.id,
    result.originalLanguage,
    result.originalTitle,
    result.overview,
    result.popularity,
    result.posterPath,
    result.releaseDate,
    result.title,
    result.video,
    result.voteAverage,
    result.voteCount
)}