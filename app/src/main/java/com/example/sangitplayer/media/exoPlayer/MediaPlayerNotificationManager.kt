package com.example.sangitplayer.media.exoPlayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.example.sangitplayer.R
import com.example.sangitplayer.media.constants.AudioConstants
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener

internal class MediaPlayerNotificationManager(
    context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: NotificationListener
) {
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        val builder = PlayerNotificationManager.Builder(context, AudioConstants.PLAYBACK_NOTIFICATION_ID, AudioConstants.PLAYBACK_NOTIFICATION_CHANNEL_ID)
         with(builder){
             setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
             setNotificationListener(notificationListener)
             setChannelNameResourceId(R.string.notification_channel)
             setChannelDescriptionResourceId(R.string.notification_channel_description)

         }
        notificationManager = builder.build()

        with(notificationManager){
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.baseline_music_note_24)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
        }

    }

    fun hideNotification(){
        notificationManager.setPlayer(null)
    }

    fun showNotification(player: Player){
        notificationManager.setPlayer(player)
    }


    inner class DescriptionAdapter(private val controller: MediaControllerCompat): PlayerNotificationManager.MediaDescriptionAdapter{
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return controller.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return controller.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return controller.metadata.description.subtitle
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }

    }

}