package com.jm.networktest

import android.content.Context
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.jm.networktest.TempDetails
import com.jm.networktest.UploadWorker

class UploadWorkFactory(private val temp: TempDetails) : WorkerFactory() {
    override fun createWorker(appContext: Context,
                              workerClassName: String,
                              workerParameters: WorkerParameters): UploadWorker {

        return UploadWorker(appContext, workerParameters, temp)
    }
}