package com.jm.networktest

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(appContext: Context, workerParameters: WorkerParameters, temp: TempDetails)
    : Worker(appContext,workerParameters) {

    private val dataBaseInstance = AppDataBase.getDatabaseInstance(appContext)
    private val args = temp
    override fun doWork(): Result {
        return try {
            dataBaseInstance.tempDataDao().insertTempData(args)
            println("resultStatus true")
            Result.success()
        }catch (e: Exception){
            println("resultStatus false")
            Result.failure()
        }
    }
}