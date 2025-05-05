package com.unimib.oases.di

import android.content.Context
import com.unimib.oases.data.repository.UserRepositoryImpl
import com.unimib.oases.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePatientRepository(
        @ApplicationContext context: Context
    ): UserRepository {
        return UserRepositoryImpl(context)
    }

}