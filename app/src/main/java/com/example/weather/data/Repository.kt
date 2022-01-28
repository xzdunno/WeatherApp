package com.example.weather.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weather.GPSTracker
import com.example.weather.MainActivity
import com.example.weather.db.AppDBDao
import com.example.weather.db.CurWeathDBDao
import com.example.weather.db.CurWeathPat
import com.example.weather.db.Hourly

import com.example.weather.model.All
import com.example.weather.model.CurrWeath
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val retroInterface: RetroInterface, private val appDBDao: AppDBDao, private val curDBDao:CurWeathDBDao) {

    fun getAllData(): LiveData<List<Hourly>> {
    return appDBDao.getAllData()
}
    fun insertAllData(hour: Hourly){
        appDBDao.insertRecord(hour)
    }
    fun getLastRec(): LiveData<CurWeathPat> {
        return curDBDao.getLastRec(1)
    }
    fun curInsertRecord(curWeath: CurWeathPat){
        curDBDao.curInsertRecord(curWeath)
    }
    fun deleteByCityName(cityName:String){
        curDBDao.deleteByCityName(cityName)
    }
    fun getLocation(context: Context):Pair<String,String>{
        val g = GPSTracker(context) //создаём трекер
        val l = g.location // получаем координаты
        if(l != null){
            val lat = l.getLatitude().toString() // широта
            val lon = l.getLongitude().toString() // долгота
            return Pair(lat,lon)
        }
        return Pair("50","50")
    }
    fun getCurWeath(options:MutableMap<String,String>,cityName: String){
        val call:Call<CurrWeath> =retroInterface.getCurWeath(options)
        call.enqueue(object: Callback<CurrWeath>{
            override fun onResponse(call: Call<CurrWeath>, response: Response<CurrWeath>) {
                if(response.isSuccessful){

                val it=response.body()!!
                    //deleteByCityName(cityName)
                    val cur= CurWeathPat(1,cityName,options.get("lat")!!,options.get("lon")!!,it.current.temp.toInt().toString(),it.current.feels_like.toInt().toString(),it.current.weather[0].icon,it.current.weather[0].main)
                    curInsertRecord(cur)

                }
            }

            override fun onFailure(call: Call<CurrWeath>, t: Throwable) {
                //
            }

        })
    }

    fun apiCall(options:MutableMap<String,String>){

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
    }}
