package ru.resodostudio.muzyakich.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.resodostudio.muzyakich.core.database.MuzDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesMuzDatabase(
        @ApplicationContext context: Context,
    ): MuzDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = MuzDatabase::class.java,
            name = "muz-database",
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
}