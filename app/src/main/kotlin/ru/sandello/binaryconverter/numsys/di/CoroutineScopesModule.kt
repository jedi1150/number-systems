package ru.sandello.binaryconverter.numsys.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineScopesModule {
    @Provides
    @Singleton
    @ApplicationScope
    fun providesCoroutineScope(
        dispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}
