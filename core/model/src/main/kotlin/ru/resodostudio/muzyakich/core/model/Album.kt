package ru.resodostudio.muzyakich.core.model

data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val year: Int?,
    val songs: List<Song>,
    val genre: String?,
)
