package ru.resodostudio.muzyakich.core.media.service

import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import javax.inject.Inject

internal class MusicSessionCallback @Inject constructor(
) : MediaSession.Callback {

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: List<MediaItem>,
    ): ListenableFuture<List<MediaItem>> = Futures.immediateFuture(
        mediaItems.map { mediaItem ->
            mediaItem.buildUpon()
                .setMediaMetadata(mediaItem.mediaMetadata)
                .setUri(mediaItem.requestMetadata.mediaUri)
                .build()
        }
    )
}
