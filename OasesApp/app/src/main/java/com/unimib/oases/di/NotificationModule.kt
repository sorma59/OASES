package com.unimib.oases.di

import android.content.Context
import com.unimib.oases.util.OasesNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): OasesNotificationManager {
        return OasesNotificationManager(context)
    }
}