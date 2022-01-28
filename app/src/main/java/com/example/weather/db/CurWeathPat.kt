package com.example.weather.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurDB")
data class CurWeathPat (
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") val id:Int=0,
    @ColumnInfo val cityName:String,
    @ColumnInfo val lat:String,
    @ColumnInfo val lon:String,
    @ColumnInfo val temp:String,
    @ColumnInfo val feels_like:String,
    @ColumnInfo val icon:String,
    @ColumnInfo val main:String
)