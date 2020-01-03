package com.jm.networktest

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TempDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTempData(data:TempDetails) : Completable

    @Query("SELECT * FROM ${TempDetails.TABLE_NAME}")
    fun getAllRecords(): Single<List<TempDetails>>

    @Delete
    fun deleteStudentData(person:TempDetails) : Completable

    @Update
    fun updateStudentData(person:TempDetails)

    @Query("SELECT COUNT(${TempDetails.ID}) FROM ${TempDetails.TABLE_NAME}")
    fun getCount(): Int
}