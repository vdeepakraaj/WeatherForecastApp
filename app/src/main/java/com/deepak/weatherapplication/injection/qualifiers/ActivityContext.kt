package com.deepak.weatherapplication.injection.qualifiers

import javax.inject.Qualifier


@Retention
@Qualifier
annotation class ActivityContext

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LastUpdateTime
