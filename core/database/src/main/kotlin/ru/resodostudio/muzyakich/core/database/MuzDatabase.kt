package ru.resodostudio.muzyakich.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.resodostudio.muzyakich.core.database.dao.SongDao
import ru.resodostudio.muzyakich.core.database.model.SongEntity
import ru.resodostudio.muzyakich.core.database.util.InstantConverter
import ru.resodostudio.muzyakich.core.database.util.UuidConverter

@Database(
    entities = [
        SongEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    UuidConverter::class,
)
internal abstract class MuzDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
}