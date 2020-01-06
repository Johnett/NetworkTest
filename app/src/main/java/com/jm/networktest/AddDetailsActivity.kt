package com.jm.networktest

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import kotlinx.android.synthetic.main.activity_add_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AddDetailsActivity : AppCompatActivity() {

    private var viewModel: AddDetailsViewModel? = null
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_details)

        viewModel = ViewModelProviders.of(this).get(AddDetailsViewModel::class.java)
        val dataBaseInstance = AppDataBase.getDatabaseInstance(this)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel?.setAllRecords()?.subscribe {
                if (it!=null){
                    viewModel?.setDetails(it)
                    println("fullValueDepiction ${it.nameFUll}")
                }
            }
        }
//        viewModel?.setAllRecords()?.observe(this, Observer {
//            if (it!=null){
//                println("tryToConvey ${it.nameFUll}")
//            }
//        })

        viewModel?.setInstanceOfDb(dataBaseInstance)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel?.getStudentData()
        }

        CoroutineScope(Dispatchers.Default).launch {
//            println("valueRetrieved ${viewModel?.getLast()?.nameFUll}")
        }

        viewModel?.studentList?.observe(this, Observer {
            viewModel?.setDetails(it.last())
            println("printAvailable ${it.last().nameFUll}")
        })

        btSubmit.setOnClickListener {
            //            val temp = saveData()
            if (isInternetAvailable(this)) {
                val initial = saveData()
                val temp = TempDetails(initial.userID, initial.nameFUll, initial.ageTotal)
                dataBaseInstance.tempDataDao().insertTempData(temp)
                println("connectStatus-- true in")

                println("connectStatus-- true out ${initial.nameFUll}")
            } else {
                val args = saveData()
                if (args.nameFUll != "none") {
                    val constraints =
                        Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build()
                    val studentDetails = workDataOf(
                        "id" to args.userID,
                        "name" to args.nameFUll,
                        "age" to args.ageTotal
                    )
                    val uploadWork = OneTimeWorkRequest.Builder(UploadWorker::class.java)
                        .setConstraints(constraints)
                        .setInputData(studentDetails)
                        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 20, TimeUnit.SECONDS)
                        .build()

                    WorkManager.getInstance(this).enqueue(uploadWork)
                    println("connectStatus-- false")
                }
            }
        }
    }

    private fun saveData(): Details {
        lateinit var pass: Details
        val name = etName.text.trim().toString()
        val age = etAge.text.trim().toString()
        etName.setText("")
        etAge.setText("")
        if (name.isBlank() || age.isBlank()) {
            Toast.makeText(this, "Please enter valid details", Toast.LENGTH_LONG).show()
            pass = Details(nameFUll = "none", ageTotal = 0)
        } else {
            val person = Details(nameFUll = name, ageTotal = age.toInt())
            viewModel?.saveDataIntoDb(person)
            Toast.makeText(this, "Details saved", Toast.LENGTH_LONG).show()
            CoroutineScope(Dispatchers.Default).launch {
//                println("valueRetrieved ${viewModel?.getLast()?.nameFUll}")
            }
            pass = Details(nameFUll = "none", ageTotal = 0)
        }
        return pass
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}
