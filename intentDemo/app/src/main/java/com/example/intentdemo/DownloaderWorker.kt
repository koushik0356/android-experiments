package com.example.intentdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class DownloaderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("DownloaderWorker", "Download work started...")
        // Simulate some background work
        Thread.sleep(2000)
        Log.d("DownloaderWorker", "Download work completed successfully.")
        return Result.success()
    }
}
