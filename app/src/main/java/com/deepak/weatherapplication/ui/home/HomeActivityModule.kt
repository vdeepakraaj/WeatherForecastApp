package com.deepak.weatherapplication.ui.home

import android.content.Context
import com.deepak.weatherapplication.injection.module.BaseActivityModule
import com.deepak.weatherapplication.injection.qualifiers.ActivityContext
import com.deepak.weatherapplication.injection.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.android.support.DaggerAppCompatActivity

@Module(includes = [BaseActivityModule::class])
abstract class HomeActivityModule {

    @Binds
    @ActivityContext
    abstract fun provideActivityContext(activity: HomeActivity): Context

    @Binds
    @ActivityScope
    abstract fun provideActivity(homeActivity: HomeActivity): DaggerAppCompatActivity
}