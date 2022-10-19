package com.example.weather.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurDB")
data class CurWeathPat(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val cityName: String,
    val lat: String,
    val lon: String,
    val temp: String,
    val feels_like: String,
    val icon: String,
    val main: String

)