package com.jm.networktest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewDetailsViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    private var dataBaseInstance: AppDataBase? = null

    var studentList = MutableLiveData<List<Details>>()

    var studentObj = Details(0, "none", 0)

    fun setInstanceOfDb(dataBaseInstance: AppDataBase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun saveDataIntoDb(data: Details) {

        dataBaseInstance?.studentDataDao()?.insertStudentData(data)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
            }, {

            })?.let {
                compositeDisposable.add(it)
            }
    }

    fun getStudentData() {

        dataBaseInstance?.studentDataDao()?.getAllRecords()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if (!it.isNullOrEmpty()) {
                    studentList.postValue(it)
                    println("headstatus some value")
                } else {
                    studentList.postValue(listOf())
                    println("headstatus no value")
                }
                it?.forEach {
                    Log.v("Person Name", it.nameFUll)
                }
//                studentObj = studentList.value?.last()!!
            }, {
            })?.let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun deletePerson(student: Details) {
        dataBaseInstance?.studentDataDao()?.deleteStudentData(student)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                //Refresh Page data
                getStudentData()
            }, {

            })?.let {
                compositeDisposable.add(it)
            }
    }

    fun getLastDetails(): Details {
        val student = Details(0, "none", 0)
//        if (studentList.value!!.isNullOrEmpty()){
//            student
//        }else {
//            studentList.value?.last()!!
//        }
        if (studentObj.userID != null) {
            println("valuebase is not null")
        } else {
            println("valuebase is null")
        }

        return student
    }

    fun getAllCount(){
        val tempCount = dataBaseInstance?.tempDataDao()?.getCount()
        val detailsCount = dataBaseInstance?.studentDataDao()?.getCount()

        if (detailsCount!! > tempCount!!){
            println("valueStatus need to be updated")
        }else {
            println("valueStatus no update needed")
        }
        println("countPrint tempdetails ${dataBaseInstance?.tempDataDao()?.getCount()}")
        println("countPrint details ${dataBaseInstance?.studentDataDao()?.getCount()}")
    }
}