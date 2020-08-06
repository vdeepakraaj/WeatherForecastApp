package com.deepak.weatherapplication.injection.component

import android.content.Context
import com.deepak.weatherapplication.BaseApplication
import com.deepak.weatherapplication.injection.module.*
import com.deepak.weatherapplication.injection.qualifiers.ApplicationContext
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelFactoryModule::class, AndroidSupportInjectionModule::class,
        ActivityBindingModule::class, NetworkModule::class, PreferenceModule::class, AppModule::class]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    @Suppress("DEPRECATION")
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<BaseApplication>() {
        @BindsInstance
        abstract fun appContext(@ApplicationContext context: Context)

        override fun seedInstance(instance: BaseApplication?) {
            instance?.applicationContext?.let { appContext(it) }
        }
    }
}
