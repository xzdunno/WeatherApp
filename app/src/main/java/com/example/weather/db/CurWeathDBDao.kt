package com.example.weather.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurWeathDBDao {
   /* @Query("select * from CurDB order by id asc")
    fun curGetAllData(): LiveData<List<CurWeathPat>>*/
    @Query("select * from CurDB where id=:id")
    fun getLastRec(id:Int):LiveData<CurWeathPat>
    /*@Query("select LAST_INSERT_ID()")
    fun getLastId():Int*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun curInsertRecord(curWeath: CurWeathPat)
    @Query("delete  from CurDB")
    fun curDeleteAll()
    @Query("delete from CurDB where cityName=:cityName")
    fun deleteByCityName(cityName:String)
}