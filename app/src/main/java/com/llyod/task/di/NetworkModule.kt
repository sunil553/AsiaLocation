package com.llyod.task.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.llyod.data.network.CountryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "https://dataservice.accuweather.com"

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NonAuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NonAuthOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CustomGsonWithTypeAdapters

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class HttpLoggingInterceptors


    @Provides
    @NonAuthRetrofit
    fun provideNonAuthRetrofit(
        @NonAuthOkHttpClient nonAuthOkHttpClient: OkHttpClient,
        @CustomGsonWithTypeAdapters gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .client(nonAuthOkHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @CustomGsonWithTypeAdapters
    fun provideGson(
    ): Gson {
        val builder = GsonBuilder()
        return builder.create()
    }

    @Provides
    @NonAuthOkHttpClient
    fun provideNonAuthOkHttp(
        @HttpLoggingInterceptors
        loggingInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @HttpLoggingInterceptors
    fun provideHttpLoggingInterceptor(): Interceptor {
        val levelType: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)
        return logging
    }


    @Provides
    @Singleton
    fun provideCountryService(@NonAuthRetrofit retrofit: Retrofit):
            CountryService = retrofit.create(
        CountryService::class.java
    )
}


