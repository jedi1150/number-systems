package ru.sandello.binaryconverter.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.sandello.binaryconverter.Settings
import ru.sandello.binaryconverter.repository.SettingsSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesSettingsDataStore(
        @ApplicationContext context: Context,
        settingsSerializer: SettingsSerializer,
    ): DataStore<Settings> =
        DataStoreFactory.create(serializer = settingsSerializer) {
            context.dataStoreFile("settings.pb")
        }
}
