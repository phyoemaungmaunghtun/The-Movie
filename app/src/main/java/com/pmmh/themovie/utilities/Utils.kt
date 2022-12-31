package com.pmmh.themovie.utilities

object Utils {
    fun posterUrlMake(uri: String?):String {
        return "https://image.tmdb.org/t/p/w780$uri"
    }
}