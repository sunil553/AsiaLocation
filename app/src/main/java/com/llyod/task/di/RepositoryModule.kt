package com.llyod.task.di

import com.llyod.data.datasource.forecast.LocationDataSource
import com.llyod.data.repository.LocationRepositoryImpl
import com.llyod.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideCountryRepository(
        locationDataSource: LocationDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(locationDataSource)
    }


}