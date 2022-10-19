package com.example.weather.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.Hour

@Dao
interface AppDBDao {

    @Query("select * from AppDB order by id asc")
    fun getAllData(): LiveData<List<Hourly>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(hourly: Hourly)

    @Query("delete from AppDB")
    fun deleteAll()
}