package ru.resodostudio.muzyakich.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.resodostudio.muzyakich.core.database.MuzDatabase
import ru.resodostudio.muzyakich.core.database.dao.SongDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesFavoriteSongDao(
        database: MuzDatabase,
    ): SongDao = database.songDao()
}