package com.unimib.oases.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import java.time.ZoneId
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimeModule {

    @Provides
    @Singleton
    fun provideClock(): Clock =
        Clock.systemUTC()

    @Provides
    @Singleton
    fun provideZoneId(): ZoneId =
        ZoneId.of("Africa/Kampala")
}
