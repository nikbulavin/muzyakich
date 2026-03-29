package ru.resodostudio.muzyakich.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.data.repository.impl.PlaylistsRepositoryImpl
import ru.resodostudio.muzyakich.core.data.repository.impl.SongsRepositoryImpl
import ru.resodostudio.muzyakich.core.data.repository.impl.UserDataRepositoryImpl
import ru.resodostudio.muzyakich.core.data.repository.util.AppLocaleManager
import ru.resodostudio.muzyakich.core.data.repository.util.AppLocaleManagerImpl
import ru.resodostudio.muzyakich.core.data.repository.util.InAppUpdateManager
import ru.resodostudio.muzyakich.core.data.repository.util.InAppUpdateManagerImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsUserDataRepository(impl: UserDataRepositoryImpl): UserDataRepository

    @Binds
    fun bindsSongsRepository(impl: SongsRepositoryImpl): SongsRepository

    @Binds
    fun bindsPlaylistsRepository(impl: PlaylistsRepositoryImpl): PlaylistsRepository

    @Binds
    fun bindsInAppUpdateManager(impl: InAppUpdateManagerImpl): InAppUpdateManager

    @Binds
    fun bindsAppLocaleManager(impl: AppLocaleManagerImpl): AppLocaleManager
}