package ru.sandello.binaryconverter.numsys.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.sandello.binaryconverter.numsys.NumSys
import ru.sandello.binaryconverter.numsys.NumberSystemDataSource

@Module
@InstallIn(SingletonComponent::class)
object NumberSystemDataSourceModule {
    @Provides
    @Singleton
    fun providesNumberSystemDataSource(numSys: NumSys): NumberSystemDataSource = NumberSystemDataSource(numSys)

    @Provides
    fun provideNumSys(): NumSys = NumSys
}
