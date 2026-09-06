package ru.resodostudio.muzyakich.core.model

data class SongMetadata(
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val albumArtist: String = "",
    val year: String = "",
    val genre: String = "",
    val trackNumber: String = "",
    val discNumber: String = "",
    val comment: String = "",
    val artworkBytes: ByteArray? = null,
    val isArtworkChanged: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongMetadata

        if (title != other.title) return false
        if (artist != other.artist) return false
        if (album != other.album) return false
        if (albumArtist != other.albumArtist) return false
        if (year != other.year) return false
        if (genre != other.genre) return false
        if (trackNumber != other.trackNumber) return false
        if (discNumber != other.discNumber) return false
        if (comment != other.comment) return false
        if (isArtworkChanged != other.isArtworkChanged) return false
        if (artworkBytes != null) {
            if (other.artworkBytes == null) return false
            if (!artworkBytes.contentEquals(other.artworkBytes)) return false
        } else if (other.artworkBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + album.hashCode()
        result = 31 * result + albumArtist.hashCode()
        result = 31 * result + year.hashCode()
        result = 31 * result + genre.hashCode()
        result = 31 * result + trackNumber.hashCode()
        result = 31 * result + discNumber.hashCode()
        result = 31 * result + comment.hashCode()
        result = 31 * result + isArtworkChanged.hashCode()
        result = 31 * result + (artworkBytes?.contentHashCode() ?: 0)
        return result
    }
}
