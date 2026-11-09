package com.example.intentdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.util.TimerTask
import java.util.Timer

class Demoservices : Service() {
    private var timer: Timer? = null
    private var count = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        Log.d("Demoservice", "Service Started")

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                count++
                Log.d("Demoservice", "D count: $count")
            }
        }, 0, 5000)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
        Log.d("Demoservice", "Service Stopped")
    }
}