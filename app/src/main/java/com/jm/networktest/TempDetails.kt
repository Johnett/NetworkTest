package com.jm.networktest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TempDetails.TABLE_NAME)
data class TempDetails(
    @PrimaryKey
    @ColumnInfo(name = ID)
    var userID: Int? = null,
    @ColumnInfo(name = NAME)
    var nameFUll: String? = null,
    @ColumnInfo(name = AGE)
    var ageTotal: Int? = null
) {
    companion object {
        const val TABLE_NAME = "temp_details"
        const val ID = "id"
        const val NAME = "name"
        const val AGE = "age"
    }
}