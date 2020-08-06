package com.deepak.weatherapplication.injection.module

import android.app.Application
import com.deepak.weatherapplication.injection.qualifiers.LastUpdateTime
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideRepository(): Application = Application()

    @Provides
    @LastUpdateTime
    //5 hours
    fun provideTime(): Long = 18000000
}