package com.example.weather.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "WeekDB")
data class WeekPat(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val dayOfWeek:String,
    val icon:String,
    val minTemp:String,
    val maxTemp:String
)
