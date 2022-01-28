package com.example.weather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [CurWeathPat::class],version=1,exportSchema = false)
abstract class CurDB:RoomDatabase(){
    abstract fun getCurWeathDBDao(): CurWeathDBDao
    companion object{
        @Volatile
        private var INSTANCE: CurDB?=null
        fun getDataBase(context: Context): CurDB {
            val tempInstance= INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    CurDB::class.java,
                    "CurDB"
                ).allowMainThreadQueries().build()
                INSTANCE =instance
                return instance
            }

        }
    }

}