package ru.resodostudio.muzyakich.core.database

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import kotlin.uuid.Uuid

internal object DatabaseMigrations {

    val Schema4to5 = object : Migration(4, 5) {
        override suspend fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `playlist_songs_new` (
                    `uuid` TEXT NOT NULL,
                    `playlist_uuid` TEXT NOT NULL,
                    `song_uuid` TEXT NOT NULL,
                    `position` INTEGER NOT NULL,
                    PRIMARY KEY(`uuid`),
                    FOREIGN KEY(`playlist_uuid`) REFERENCES `playlists`(`uuid`) ON DELETE CASCADE,
                    FOREIGN KEY(`song_uuid`) REFERENCES `songs`(`uuid`) ON DELETE CASCADE
                )
                """.trimIndent()
            )

            val existingRows = buildList {
                connection.prepare(
                    "SELECT `playlist_uuid`, `song_uuid`, `position` FROM `playlist_songs`"
                ).use { statement ->
                    while (statement.step()) {
                        add(
                            Triple(
                                statement.getText(0),
                                statement.getText(1),
                                statement.getInt(2),
                            )
                        )
                    }
                }
            }

            connection.prepare(
                "INSERT INTO `playlist_songs_new` (`uuid`, `playlist_uuid`, `song_uuid`, `position`) " +
                        "VALUES (?, ?, ?, ?)"
            ).use { statement ->
                for ((playlistUuid, songUuid, position) in existingRows) {
                    statement.bindText(1, Uuid.random().toHexString())
                    statement.bindText(2, playlistUuid)
                    statement.bindText(3, songUuid)
                    statement.bindInt(4, position)
                    statement.step()
                    statement.reset()
                }
            }

            connection.execSQL("DROP TABLE `playlist_songs`")
            connection.execSQL("ALTER TABLE `playlist_songs_new` RENAME TO `playlist_songs`")

            connection.execSQL(
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_playlist_songs_playlist_uuid_position` " +
                        "ON `playlist_songs` (`playlist_uuid`, `position`)"
            )
            connection.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_playlist_songs_song_uuid` " +
                        "ON `playlist_songs` (`song_uuid`)"
            )
        }
    }
}
