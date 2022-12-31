package com.pmmh.themovie.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pmmh.themovie.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val timeoutConnect = 300  //In seconds
    private const val timeoutRead = 300   //In seconds
    private const val BASE_URL = "https://api.themoviedb.org/"


    @Singleton
    @Provides
    fun provideOkHttpClient(
        headerInterceptor: okhttp3.Interceptor,
        logger: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(logger)
            .connectTimeout(timeoutConnect.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeoutRead.toLong(), TimeUnit.SECONDS).build()

    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideLogger(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.apply {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            }.level = HttpLoggingInterceptor.Level.BODY
        }
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideInterceptor(): okhttp3.Interceptor {
        return Interceptor()
    }
}