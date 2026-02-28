package ru.resodostudio.muzyakich.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.resodostudio.muzyakich.core.database.dao.FavoriteSongDao
import ru.resodostudio.muzyakich.core.database.model.FavoriteSongEntity

@Database(
    entities = [
        FavoriteSongEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
internal abstract class MuzDatabase : RoomDatabase() {

    abstract fun favoriteSongDao(): FavoriteSongDao
}