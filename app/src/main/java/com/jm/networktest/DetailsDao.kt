package com.jm.networktest

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentData(data:Details) : Completable

    @Query("SELECT * FROM ${Details.TABLE_NAME}")
    fun getAllRecords(): Single<List<Details>>

    @Delete
    fun deleteStudentData(person:Details) : Completable

    @Update
    fun updateStudentData(person:Details)

    @Query("SELECT COUNT(${Details.ID}) FROM ${Details.TABLE_NAME}")
    fun getCount(): Int

    @Query("SELECT * FROM ${Details.TABLE_NAME} ORDER BY ${Details.ID} DESC LIMIT 1")
    fun getLastRecord(): Flowable<Details>
}