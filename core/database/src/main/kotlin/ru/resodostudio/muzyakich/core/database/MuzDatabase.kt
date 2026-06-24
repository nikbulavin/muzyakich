package ru.resodostudio.muzyakich.core.database

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import ru.resodostudio.muzyakich.core.database.dao.PlaylistDao
import ru.resodostudio.muzyakich.core.database.dao.SongDao
import ru.resodostudio.muzyakich.core.database.model.PlaylistEntity
import ru.resodostudio.muzyakich.core.database.model.PlaylistSongCrossRef
import ru.resodostudio.muzyakich.core.database.model.SongEntity
import ru.resodostudio.muzyakich.core.database.util.InstantConverter
import ru.resodostudio.muzyakich.core.database.util.UuidConverter

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class,
    ],
    version = 4,
    exportSchema = true,
)
@ColumnTypeConverters(
    InstantConverter::class,
    UuidConverter::class,
)
internal abstract class MuzDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    abstract fun playlistDao(): PlaylistDao
}