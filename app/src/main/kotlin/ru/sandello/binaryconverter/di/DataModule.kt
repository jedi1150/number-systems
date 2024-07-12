package ru.sandello.binaryconverter.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sandello.binaryconverter.repository.OfflineSettingsRepository
import ru.sandello.binaryconverter.repository.SettingsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsSettingsDataRepository(
        settingsRepository: OfflineSettingsRepository,
    ): SettingsRepository
}
