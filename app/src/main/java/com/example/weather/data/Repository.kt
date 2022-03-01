package com.example.weather.data

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.HourAdapter
//import com.example.weather.GPSTracker
import com.example.weather.db.*

import com.example.weather.model.All
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
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
    fun apiCall(options:MutableMap<String,String>,cityName: String){
        options.put("exclude","minutely,alerts")
val call:Call<All> =retroInterface.getDataHour(options)
        call.enqueue(object: Callback<All>{
            override fun onResponse(call: Call<All>, response: Response<All>) {
                if(response.isSuccessful){
                    val it=response.body()!!
                    val cur= CurWeathPat(1, cityName,options.get("lat")!!,options.get("lon")!!,it.current.temp.toInt().toString(),it.current.feels_like.toInt().toString(),it.current.weather[0].icon,it.current.weather[0].main)
                    curDBDao.curInsertRecord(cur)

                    appDBDao.deleteAll()
                    val time=SimpleDateFormat("HH").format(Calendar.getInstance().time).toInt()
                    for(i in 1..24)
                        {val it1= response.body()?.hourly!![i]
                            val timeStr:String
                            if(time+i>=24){
                                if(time+i-24<10) {
                                    timeStr="0"+(time+i-24).toString()+":00"
                                }
                                else {timeStr=(time+i-24).toString()+":00"}
                            }
                            else{
                                if(time+i<10) timeStr="0"+(time+i).toString()+":00"
                                else timeStr=(time+i).toString()+":00"}
                        val hourly=Hourly(i,it1.temp,it1.humidity, timeStr,it1.weather[0].icon)
                        insertAllData(hourly)}

                    weekDBDao.deleteAll()
                    for(i in 0..6){
                        val sdf = SimpleDateFormat("EEEE")
                        val d = Date()
                        val dayOfWeek= sdf.format(d.time+i*1000*60*60*24)
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        val it1=response.body()?.daily!![i]
                        weekDBDao.insertRecord(WeekPat(i+1,dayOfWeek,it1.weather[0].icon,it1.temp.min.toInt().toString(),it1.temp.max.toInt().toString()))
                    }

                }
            }

            override fun onFailure(call: Call<All>, t: Throwable) {
                //
            }

        })
    }
}
