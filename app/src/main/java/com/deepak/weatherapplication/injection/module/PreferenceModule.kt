package com.deepak.weatherapplication.injection.module

import android.content.Context
import android.content.SharedPreferences
import com.deepak.weatherapplication.injection.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceModule {
    private val PREF_NAME = "prefs"

    @Provides
    @Singleton
    internal fun providesSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

}
