package ru.resodostudio.muzyakich.core.model

data class Artist(
    val id: Long,
    val name: String,
    val songs: List<Song>,
)
