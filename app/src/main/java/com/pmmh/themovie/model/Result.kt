package com.pmmh.themovie.model

import com.google.gson.annotations.SerializedName
import com.pmmh.themovie.local.entities.LocalMovies

data class Result(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("video")
    val video: Boolean?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    var movieType: Int?
){constructor(localMovies:LocalMovies):this(
    localMovies.adult,
    localMovies.backdropPath,
    localMovies.genreIds,
    localMovies.id,
    localMovies.originalLanguage,
    localMovies.originalTitle,
    localMovies.overview,
    localMovies.popularity,
    localMovies.posterPath,
    localMovies.releaseDate,
    localMovies.title,
    localMovies.video,
    localMovies.voteAverage,
    localMovies.voteCount,
    localMovies.movieType
)}