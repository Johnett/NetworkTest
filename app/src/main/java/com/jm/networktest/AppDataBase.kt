package com.jm.networktest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Details::class, TempDetails::class], version = DB_VERSION)
abstract class AppDataBase : RoomDatabase() {
    abstract fun studentDataDao(): DetailsDao
    abstract fun tempDataDao(): TempDetailsDao

    companion object {
        @Volatile
        private var databaseInstance: AppDataBase? = null

        fun getDatabaseInstance(mContext: Context): AppDataBase =
            databaseInstance ?: synchronized(this) {
                databaseInstance ?: buildDatabaseInstance(mContext).also {
                    databaseInstance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, AppDataBase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}

const val DB_VERSION = 2

const val DB_NAME = "AppDataBase.db"