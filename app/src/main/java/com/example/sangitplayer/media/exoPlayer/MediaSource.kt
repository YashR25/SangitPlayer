package com.example.sangitplayer.media.exoPlayer

import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.sangitplayer.data.repository.AudioRepository
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import javax.inject.Inject

class MediaSource @Inject constructor(private val repository: AudioRepository) {
    private val onReadyListeners: MutableList<OnReadyListener> = mutableListOf()
    var audioMediaMetaData: List<MediaMetadataCompat> = listOf()

    private var state: AudioSourceState = AudioSourceState.STATE_CREATED
    set(value) {
        if(state == AudioSourceState.STATE_CREATED || state == AudioSourceState.STATE_ERROR){
            synchronized(onReadyListeners){
                field = value
                onReadyListeners.forEach{onReadyListeners->
                    onReadyListeners.invoke(isReady)
                }
            }
        }else{
            field = value
        }
    }

    private val isReady: Boolean
        get() = state == AudioSourceState.STATE_INITIALIZED

    suspend fun load(){
        state = AudioSourceState.STATE_INITIALIZING
        val data = repository.getAudioData()
        audioMediaMetaData = data.map { audio ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audio.id.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, audio.artist.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audio.uri.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.displayName.toString())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, audio.duration.toLong())
                .build()
        }
        state = AudioSourceState.STATE_INITIALIZED
    }

    //for ExoPlayer
    fun asMediaSource(dataSourceFactory: CacheDataSource.Factory): ConcatenatingMediaSource{
        val concatenatingMediaSource = ConcatenatingMediaSource()
        audioMediaMetaData.forEach{mediaMetadataCompat ->  
            val mediaItem = MediaItem.fromUri(mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI))
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    //for Client
    fun asMediaItem() = audioMediaMetaData.map {metadata->
        val description = MediaDescriptionCompat.Builder()
            .setTitle(metadata.description.title)
            .setMediaId(metadata.description.mediaId)
            .setSubtitle(metadata.description.subtitle)
            .setMediaUri(metadata.description.mediaUri)
            .build()
        android.support.v4.media.MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)
    }.toMutableList()

    fun refresh(){
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }

    fun whenReady(onReadyListener: OnReadyListener): Boolean{
        return if (state == AudioSourceState.STATE_INITIALIZING || state == AudioSourceState.STATE_CREATED){
            onReadyListeners += onReadyListener
            false
        }else{
            onReadyListener.invoke(isReady)
            true
        }
    }
}





typealias OnReadyListener = (Boolean) -> Unit

enum class AudioSourceState{
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}