package com.example.sangitplayer.media.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.sangitplayer.R
import com.example.sangitplayer.media.constants.AudioConstants
import com.example.sangitplayer.media.exoPlayer.MediaPlayerNotificationManager
import com.example.sangitplayer.media.exoPlayer.MediaSource
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*

@AndroidEntryPoint
class MediaPlayerService: MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: CacheDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var mediaSource: MediaSource

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var session: MediaSessionCompat
    private lateinit var sessionConnector: MediaSessionConnector

    private lateinit var notificationManager: MediaPlayerNotificationManager

    private var currentPlayingMedia: MediaMetadataCompat? = null
    private val isPlayerInitialized = false
    private var isForeground = false

    companion object{
        private const val TAG = "MediaPlayerService"
        var currentDuration: Long = 0L
            private set
    }

    override fun onCreate() {
        super.onCreate()
        val sessionActivityIntent = packageManager?.
                getLaunchIntentForPackage(packageName)?.
                let {sessionIntent->
                    PendingIntent.getActivity(this,0,sessionIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                }
        session = MediaSessionCompat(this, TAG).apply {
            setSessionActivity(sessionActivityIntent)
            isActive = true
        }

        sessionToken = session.sessionToken

        notificationManager = MediaPlayerNotificationManager(this,
            session.sessionToken,
            PlayerNotificationListener()
        )

        serviceScope.launch {
            mediaSource.load()
        }

        sessionConnector = MediaSessionConnector(session).apply {
            setPlaybackPreparer(PlaybackPreparer())
            setQueueNavigator(QueueNavigator(session))
            setPlayer(exoPlayer)
        }

        notificationManager.showNotification(exoPlayer)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(AudioConstants.MEDIA_ROOT_ID,null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when(parentId){
            AudioConstants.MEDIA_ROOT_ID -> {
                val resultSent = mediaSource.whenReady {isReady->
                    if(isReady){
                        result.sendResult(mediaSource.asMediaItem())
                    }else{
                        result.sendResult(null)
                    }
                }
                if(!resultSent){
                    result.detach()
                }
            }
            else -> Unit
        }


    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        when(action){
            AudioConstants.START_MEDIA_PLAY_ACTION -> {
                notificationManager.showNotification(exoPlayer)
            }
            AudioConstants.REFRESH_MEDIA_PLAY_ACTION -> {
                mediaSource.refresh()
                notifyChildrenChanged(AudioConstants.MEDIA_ROOT_ID)
            }
            else -> Unit
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.release()
    }



    inner class PlayerNotificationListener: PlayerNotificationManager.NotificationListener{
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopForeground(true)
            stopSelf()
            isForeground = false
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            if(ongoing && !isForeground){
                ContextCompat.startForegroundService(applicationContext,
                Intent(applicationContext, MediaPlayerService::class.java))
                startForeground(notificationId, notification)
            }
        }
    }

    inner class PlaybackPreparer: MediaSessionConnector.PlaybackPreparer{
        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ): Boolean {
            return false
        }

        override fun getSupportedPrepareActions(): Long {
            return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
        }

        override fun onPrepare(playWhenReady: Boolean) = Unit

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            mediaSource.whenReady {isReady->
                if(isReady){
                    val itemToPlay = mediaSource.audioMediaMetaData.find {
                        it.description.mediaId == mediaId
                    }
                    currentPlayingMedia = itemToPlay
                    preparePlayer(mediaSource.audioMediaMetaData,
                    itemToPlay,
                    playWhenReady)
                }
            }
        }

        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

    }

    inner class QueueNavigator(mediaSessionCompat: MediaSessionCompat): TimelineQueueNavigator(mediaSessionCompat){
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            if (windowIndex < mediaSource.audioMediaMetaData.size){
                return mediaSource.audioMediaMetaData[windowIndex].description
            }
            return MediaDescriptionCompat.Builder().build()
        }


    }

    fun preparePlayer(
        mediaMetadataCompat: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean
    ){
        val indexToPlay = if(currentPlayingMedia == null) 0 else mediaMetadataCompat.indexOf(itemToPlay)

        exoPlayer.addListener(PlayerEventListener())
        exoPlayer.setMediaSource(mediaSource.asMediaSource(dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(indexToPlay, 0)
        exoPlayer.playWhenReady = playWhenReady

    }

    inner class PlayerEventListener: Player.Listener{
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when(playbackState){
                Player.STATE_BUFFERING, Player.STATE_READY -> {
                    notificationManager.showNotification(exoPlayer)
                }
                else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            currentDuration = player.duration
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            var message = R.string.generic_error

            if (error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND){
                message = R.string.stringerror_media_not_found
            }
            Toast.makeText(this@MediaPlayerService,message,Toast.LENGTH_SHORT).show()
        }

    }

}