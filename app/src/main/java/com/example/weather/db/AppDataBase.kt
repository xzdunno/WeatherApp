package com.example.weather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.model.Hour

@Database(entities = [Hourly::class,WeekPat::class,CurWeathPat::class],version=1,exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getAppDBDao(): AppDBDao
    abstract fun getWeekDBDao():WeekDBDao
    abstract fun getCurWeathDBDao(): CurWeathDBDao
    companion object{
        @Volatile
        private var INSTANCE: AppDataBase?=null
        fun getDataBase(context: Context): AppDataBase {
            val tempInstance= INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "AppDB"
                ).allowMainThreadQueries().build()
                INSTANCE =instance
                return instance
            }

        }
    }
}