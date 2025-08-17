package ru.resodostudio.muzyakich.core.model.data

data class Artist(
    val id: Long,
    val name: String,
    val songs: List<Song>,
)
