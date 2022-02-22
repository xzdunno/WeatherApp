package com.example.weather.di

import android.app.Application
import android.content.Context
import com.example.weather.data.RetroInterface
import com.example.weather.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    val BASE_URL="https://api.openweathermap.org/"

    @Provides
    @Singleton
    fun getDB(context: Application): AppDataBase {
        return AppDataBase.getDataBase(context)
    }
    @Provides
    @Singleton
    fun getAppDBDao(database: AppDataBase): AppDBDao {
        return database.getAppDBDao()
    }
    @Provides
    @Singleton
    fun getCurDB(context: Application): CurDB {
        return CurDB.getDataBase(context)
    }
    @Provides
    @Singleton
    fun getCurWeathDBDao(database: CurDB): CurWeathDBDao {
        return database.getCurWeathDBDao()
    }
    @Provides
    @Singleton
    fun getWeekDB(context: Application): WeekDataBase {
        return WeekDataBase.getWeekDB(context)
    }
    @Provides
    @Singleton
    fun getWeekDBDao(database: WeekDataBase):WeekDBDao  {
        return database.getWeekDBDao()
    }
    @Provides
    @Singleton
    fun getRetroInterface(retrofit: Retrofit): RetroInterface {
        return retrofit.create(RetroInterface::class.java)
    }
    @Provides
    @Singleton
    fun getNetworkInstance():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}