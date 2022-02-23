package com.example.weather.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppDB")
data class Hourly(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id")val id: Int = 0,
    val temp:String,
    val humidity:String,
    val time:String,
    val picture:String
)