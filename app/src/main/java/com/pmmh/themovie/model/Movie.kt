package com.pmmh.themovie.model

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("dates")
    val dates: Dates = Dates(),
    @SerializedName("page")
    val page: Int = -1,
    @SerializedName("results")
    val results: List<Result> = emptyList(),
    @SerializedName("total_pages")
    val totalPages: Int = -1,
    @SerializedName("total_results")
    val totalResults: Int = -1
)