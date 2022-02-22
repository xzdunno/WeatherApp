package com.example.weather.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurWeathDBDao {
    @Query("select * from CurDB where id=:id")
    fun getLastRec(id:Int):LiveData<CurWeathPat>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun curInsertRecord(curWeath: CurWeathPat)
    @Query("delete from CurDB")
    fun curDeleteAll()
    @Query("delete from CurDB where cityName=:cityName")
    fun deleteByCityName(cityName:String)
    @Query("select * from CurDB where id=:id")
    fun getCoords(id:Int):CurWeathPat
}