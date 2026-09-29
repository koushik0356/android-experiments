package com.example.intentdemo

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.widget.Toast
import androidx.core.app.NotificationCompat

/**
 * MusicService handles the background audio playback and persistent notification.
 * It allows music to continue playing even when the user minimizes the application.
 */
class MusicService : Service() {
    
    // Core playback engine
    private var mediaPlayer: MediaPlayer? = null
    
    // Resource pointers for the available tracks
    private val musicResourceList = intArrayOf(R.raw.music1, R.raw.music2, R.raw.music3)
    private val musicNameList = arrayOf("Music 1", "Music 2", "Music 3")
    
    // Tracking current playback state
    private var currentSongIndex = 0
    private val NOTIFICATION_CHANNEL_ID = "playback_channel"
    private val NOTIFICATION_ID = 101

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
    }

    /**
     * Entry point for commands sent from the UI (Start, Play/Pause, Next, Stop).
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: return START_NOT_STICKY
        
        when (action) {
            "START" -> {
                val selectedResourceId = intent.getIntExtra("MUSIC_RES_ID", -1)
                currentSongIndex = musicResourceList.indexOf(selectedResourceId).coerceAtLeast(0)
                startNewTrack(selectedResourceId, musicNameList[currentSongIndex])
            }
            "PLAY_PAUSE" -> {
                togglePlayback()
            }
            "NEXT" -> {
                playNextTrack()
            }
            "STOP" -> {
                stopMusicService()
            }
        }
        return START_NOT_STICKY
    }

    private fun togglePlayback() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            Toast.makeText(this, "Music Paused", Toast.LENGTH_SHORT).show()
        } else {
            mediaPlayer?.start()
            Toast.makeText(this, "Music Resumed", Toast.LENGTH_SHORT).show()
        }
        updateNotification(musicNameList[currentSongIndex])
    }

    private fun playNextTrack() {
        // Move to the next index and wrap around to zero if at the end
        currentSongIndex = (currentSongIndex + 1) % musicResourceList.size
        startNewTrack(musicResourceList[currentSongIndex], musicNameList[currentSongIndex])
    }

    private fun startNewTrack(resourceId: Int, trackName: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resourceId).apply {
            // Automatically skip to the next song when the current one ends
            setOnCompletionListener { playNextTrack() }
            start()
        }
        updateNotification(trackName)
        Toast.makeText(this, "Playing: $trackName", Toast.LENGTH_SHORT).show()
    }

    private fun stopMusicService() {
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        mediaPlayer = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Toast.makeText(this, "Music Stopped", Toast.LENGTH_SHORT).show()
    }

    /**
     * Builds and displays a foreground notification with playback controls.
     */
    private fun updateNotification(trackTitle: String) {
        val playPauseIcon = if (mediaPlayer?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
        val playPauseLabel = if (mediaPlayer?.isPlaying == true) "Pause" else "Play"

        // Create explicit intents for each control action
        val playPauseIntent = Intent(this, MusicService::class.java).apply { action = "PLAY_PAUSE" }
        val nextIntent = Intent(this, MusicService::class.java).apply { action = "NEXT" }
        val stopIntent = Intent(this, MusicService::class.java).apply { action = "STOP" }

        // Wrap intents in PendingIntents for the notification system
        val playPausePI = PendingIntent.getService(this, 10, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val nextPI = PendingIntent.getService(this, 20, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val stopPI = PendingIntent.getService(this, 30, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(trackTitle)
            .setContentText("Foreground Media Player")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOnlyAlertOnce(true) // Prevent sound/vibrate on every toggle
            .setOngoing(mediaPlayer?.isPlaying == true)
            .addAction(playPauseIcon, playPauseLabel, playPausePI)
            .addAction(R.drawable.ic_next, "Next", nextPI)
            .addAction(R.drawable.ic_stop, "Stop", stopPI)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Music Playback Controls"
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}