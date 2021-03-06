package com.jm.networktest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddDetailsViewModel : ViewModel() {
//    var liveLast = LiveData<Details>()

    protected val compositeDisposable = CompositeDisposable()

    private var dataBaseInstance: AppDataBase? = null

    var studentList = MutableLiveData<List<Details>>()

    var lastDetails = Details(0,"none",0)

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
                } else {
                    studentList.postValue(listOf())
                }
                it?.forEach {
                    Log.v("Person Name", it.nameFUll)
                }
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

    fun getLastItem():Details {
        return studentList.value?.last()!!
    }

    fun setDetails(details: Details){
        lastDetails = details
    }
    fun getDetails(): Details{
        return lastDetails
    }
//    fun retrieveLast(): LiveData<Details> {
//        return liveLast
//    }

    fun setAllRecords():Flowable<Details> = dataBaseInstance?.studentDataDao()?.getLastRecord()!!


}