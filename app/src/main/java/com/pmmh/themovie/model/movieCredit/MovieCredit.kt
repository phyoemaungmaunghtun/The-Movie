package com.pmmh.themovie.model.movieCredit


import com.google.gson.annotations.SerializedName

data class MovieCredit(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)