package com.pmmh.themovie.di

import okhttp3.Interceptor
import okhttp3.Response

class Interceptor: Interceptor {
    private val contentType = "Content-Type"
    private val contentTypeValue = "application/json"
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
        builder.header(contentType, contentTypeValue)
        builder.header("Accept-Language", "en")
        builder.method(original.method, original.body)
        return chain.proceed(builder.build())
    }

}