package ru.sandello.binaryconverter.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import numsys.NumSys

@Module
@InstallIn(SingletonComponent::class)
object NumSysModule {

    @Provides
    fun provideNumSys(): NumSys {
        return NumSys
    }
}
