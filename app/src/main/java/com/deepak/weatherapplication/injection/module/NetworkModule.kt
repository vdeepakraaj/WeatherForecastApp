package com.deepak.weatherapplication.injection.module

import com.deepak.weatherapplication.data.services.CoroutineApiService
import com.deepak.weatherapplication.AppConstants
import com.deepak.weatherapplication.repo.WeatherRepo
import com.deepak.weatherforecast.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpBuilder.interceptors()
                .add(httpLoggingInterceptor)
        }
        return httpBuilder.build()
    }

    @Provides
    @Singleton
    @Named(AppConstants.COROUTINE_RETROFIT)
    internal fun provideCoroutineRestAdapter(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideCoroutineApiService(@Named(AppConstants.COROUTINE_RETROFIT) restAdapter: Retrofit): CoroutineApiService {
        return restAdapter.create(CoroutineApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideCoroutineJokeRepo(coroutineApiService: CoroutineApiService): WeatherRepo {
        return WeatherRepo(coroutineApiService)
    }
}
