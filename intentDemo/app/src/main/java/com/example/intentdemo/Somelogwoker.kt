package com.example.intentdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class Somelogwoker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("Somelogwoker", "Worker is doing work")
        return Result.success()
    }
}
