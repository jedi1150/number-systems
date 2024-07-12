package ru.sandello.binaryconverter.numsys.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import numsys.NumSys
import ru.sandello.binaryconverter.numsys.NumberSystemDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NumberSystemDataSourceModule {

    @Provides
    @Singleton
    fun providesNumberSystemDataSource(
        numSys: NumSys,
    ): NumberSystemDataSource = NumberSystemDataSource(numSys)

    @Provides
    fun provideNumSys(): NumSys = NumSys
}
