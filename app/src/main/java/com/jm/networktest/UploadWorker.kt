package com.jm.networktest

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(appContext: Context, workerParameters: WorkerParameters, temp: TempDetails) :
    Worker(appContext, workerParameters) {

    private val dataBaseInstance = AppDataBase.getDatabaseInstance(appContext)
    override fun doWork(): Result {
        return try {
            val arg = TempDetails(
                inputData.getInt("id", 0),
                inputData.getString("name"),
                inputData.getInt("age", 0)
            )
            dataBaseInstance.tempDataDao().insertTempData(arg)
            println("resultStatus true")
            Result.success()
        } catch (e: Exception) {
            println("resultStatus false")
            Result.failure()
        }
    }
}