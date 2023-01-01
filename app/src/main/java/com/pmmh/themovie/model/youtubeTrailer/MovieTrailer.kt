package com.pmmh.themovie.model.youtubeTrailer


import com.google.gson.annotations.SerializedName

data class MovieTrailer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<Result>
)