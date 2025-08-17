package ru.resodostudio.muzyakich.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.resodostudio.muzyakich.core.data.repository.ArtistsRepository
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.data.repository.impl.SongsRepositoryImpl
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.data.repository.impl.ArtistsRepositoryImpl
import ru.resodostudio.muzyakich.core.data.repository.impl.UserDataRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    internal abstract fun bindsUserDataRepository(
        impl: UserDataRepositoryImpl,
    ): UserDataRepository

    @Binds
    internal abstract fun bindsSongsRepository(
        impl: SongsRepositoryImpl,
    ): SongsRepository

    @Binds
    internal abstract fun bindsArtistsRepository(
        impl: ArtistsRepositoryImpl,
    ): ArtistsRepository
}