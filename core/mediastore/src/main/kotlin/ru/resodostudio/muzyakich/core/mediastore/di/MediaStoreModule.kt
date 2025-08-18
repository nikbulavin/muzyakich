package ru.resodostudio.muzyakich.core.mediastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSource
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MediaStoreModule {

    @Binds
    internal abstract fun bindsMediaStoreDataSource(
        impl: MediaStoreDataSourceImpl,
    ): MediaStoreDataSource
}