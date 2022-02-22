package com.example.weather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [WeekPat::class],version = 1,exportSchema = false)
abstract class WeekDataBase:RoomDatabase() {
abstract fun getWeekDBDao():WeekDBDao
    companion object{
        @Volatile
        private var INSTANCE: WeekDataBase?=null
        fun getWeekDB(context: Context): WeekDataBase {
            val tempInstance= INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    WeekDataBase::class.java,
                    "WeekDB"
                ).allowMainThreadQueries().build()
                INSTANCE =instance
                return instance
            }

        }
    }
}