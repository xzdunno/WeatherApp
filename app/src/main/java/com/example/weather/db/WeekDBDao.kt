package com.example.weather.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeekDBDao {
    @Query("select * from WeekDB order by id asc")
    fun getAllData(): LiveData<List<WeekPat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(week: WeekPat)

    @Query("delete from WeekDB")
    fun deleteAll()
}