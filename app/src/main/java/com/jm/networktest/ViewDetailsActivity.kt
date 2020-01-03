package com.jm.networktest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_view_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewDetailsActivity : AppCompatActivity() {

    private var viewModel: ViewDetailsViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)

        viewModel = ViewModelProviders.of(this).get(ViewDetailsViewModel::class.java)
        val dataBaseInstance = AppDataBase.getDatabaseInstance(this)
        viewModel?.setInstanceOfDb(dataBaseInstance)

        viewModel?.getStudentData()

//        val student = viewModel?.getLastDetails()

        viewModel?.studentList?.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                val student = it.last()
                tvId.text = student.userID.toString()
                tvName.text = student.nameFUll
                tvAge.text = student.ageTotal.toString()
            }else{
                tvId.text = "none"
                tvName.text = "none"
                tvAge.text = "none"
            }
        })

        CoroutineScope(Dispatchers.Default).launch {
            viewModel?.getAllCount()
        }

//        tvId.text = student?.userID.toString()
//        tvName.text = student?.nameFUll
//        tvAge.text = student?.ageTotal.toString()

//        println("last_student ${viewModel?.getStudentData()?.nameFUll}")
    }
}
