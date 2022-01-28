package com.example.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppDB")
data class Hour(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id")val id: Int = 0,
    @ColumnInfo val temp:String,
    @ColumnInfo val humidity:String,
    @ColumnInfo val weather:List<Icon>)
