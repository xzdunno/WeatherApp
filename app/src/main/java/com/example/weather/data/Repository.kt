package com.example.weather.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
//import com.example.weather.GPSTracker
import com.example.weather.MainActivity
import com.example.weather.db.*

import com.example.weather.model.All
import com.example.weather.model.CurrWeath
import com.example.weather.model.Week
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val retroInterface: RetroInterface, private val appDBDao: AppDBDao, private val curDBDao:CurWeathDBDao,private val weekDBDao: WeekDBDao) {
fun getCoords():CurWeathPat{
    return curDBDao.getCoords(1)
}
    fun getAllData(): LiveData<List<Hourly>> {
    return appDBDao.getAllData()
}
    fun getAllDataWeek(): LiveData<List<WeekPat>> {
        return weekDBDao.getAllData()
    }
    fun insertAllData(hour: Hourly){
        appDBDao.insertRecord(hour)
    }
    fun getLastRec(): LiveData<CurWeathPat> {
        return curDBDao.getLastRec(1)
    }
    fun getCurWeath(options:MutableMap<String,String>,cityName: String){
        options.put("exclude","minutely,daily,alerts,hourly")
        val call:Call<CurrWeath> =retroInterface.getCurWeath(options)
        call.enqueue(object: Callback<CurrWeath>{
            override fun onResponse(call: Call<CurrWeath>, response: Response<CurrWeath>) {
                if(response.isSuccessful){

                val it=response.body()!!
                    val cur= CurWeathPat(1, cityName,options.get("lat")!!,options.get("lon")!!,it.current.temp.toInt().toString(),it.current.feels_like.toInt().toString(),it.current.weather[0].icon,it.current.weather[0].main)
                    curDBDao.curInsertRecord(cur)
                }
            }

            override fun onFailure(call: Call<CurrWeath>, t: Throwable) {
                //
            }

        })
    }
fun getWeekWeath(options: MutableMap<String, String>){
    options.put("exclude","minutely,alerts,hourly,current")
    val call:Call<Week> =retroInterface.getWeekWeath(options)
    call.enqueue(object: Callback<Week>{
        override fun onResponse(call: Call<Week>, response: Response<Week>) {
            if(response.isSuccessful){
                weekDBDao.deleteAll()
                for(i in 0..6){
                    val it=response.body()?.daily!![i]
                    weekDBDao.insertRecord(WeekPat(i,"Понедельник",it.weather[0].icon,it.temp.min.toInt().toString(),it.temp.max.toInt().toString()))
                }
            }
        }

        override fun onFailure(call: Call<Week>, t: Throwable) {

        }
    })
}
    fun apiCall(options:MutableMap<String,String>){
        options.put("exclude","minutely,daily,alerts")
val call:Call<All> =retroInterface.getDataHour(options)
        call.enqueue(object: Callback<All>{
            override fun onResponse(call: Call<All>, response: Response<All>) {
                if(response.isSuccessful){
                    appDBDao.deleteAll()
                    for(i in 0..23)
                        {val it= response.body()?.hourly!![i]
                        val hourly=Hourly(i,it.temp,it.humidity, it.weather[0].icon)
                        insertAllData(hourly)}

                }
            }

            override fun onFailure(call: Call<All>, t: Throwable) {
                //
            }

        })
    }
}
