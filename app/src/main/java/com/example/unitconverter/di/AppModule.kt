package com.example.unitconverter.di

import com.example.unitconverter.domain.TemperatureConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTemperatureConverter(): TemperatureConverter {
        return TemperatureConverter()
    }
}