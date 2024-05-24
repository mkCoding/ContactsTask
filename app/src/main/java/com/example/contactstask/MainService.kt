package com.example.contactstask

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast

class MainService:  Service() {

    private var musicPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        musicPlayer = MediaPlayer.create(this, R.raw.sample_music)
        musicPlayer!!.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Music Service started by user.", Toast.LENGTH_LONG).show()
        musicPlayer!!.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer!!.stop()
        Toast.makeText(this, "Music Service destroyed by user.", Toast.LENGTH_LONG).show()    }
}
