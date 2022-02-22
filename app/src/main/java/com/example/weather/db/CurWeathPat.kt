package com.example.weather.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurDB")
data class CurWeathPat (
    @PrimaryKey(autoGenerate = true) val id:Int,
     val cityName:String,
     val lat:String,
     val lon:String,
     val temp:String,
     val feels_like:String,
     val icon:String,
     val main:String
)
/*@ColumnInfo(name="city_name") val cityName:String,
@ColumnInfo(name="lat") val lat:String,
@ColumnInfo(name="lon") val lon:String,
@ColumnInfo(name="temp") val temp:String,
@ColumnInfo(name="feels_like") val feels_like:String,
@ColumnInfo(name="icon") val icon:String,
@ColumnInfo(name="main")*/