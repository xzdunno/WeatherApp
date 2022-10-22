package com.example.weather.data

import android.os.Handler
import androidx.lifecycle.LiveData
//import com.example.weather.GPSTracker
import com.example.weather.db.*

import com.example.weather.model.All
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val retroInterface: RetroInterface,
    private val appDBDao: AppDBDao,
    private val curDBDao: CurWeathDBDao,
    private val weekDBDao: WeekDBDao
) {
    private val handler=Handler()

    fun getCoords(): CurWeathPat {
        return curDBDao.getCoords(1)
    }

    fun getAllData(): LiveData<List<Hourly>> {
        return appDBDao.getAllData()
    }

    fun getAllDataWeek(): LiveData<List<WeekPat>> {
        return weekDBDao.getAllData()
    }

    fun insertAllData(hour: Hourly) {
        appDBDao.insertRecord(hour)
    }

    fun getLastRec(): LiveData<CurWeathPat> {
        return curDBDao.getLastRec(1)
    }

    fun apiCall(options: MutableMap<String, String>, cityName: String) {
        options.put("exclude", "minutely,alerts")
        val call: Call<All> = retroInterface.getDataHour(options)
        call.enqueue(object : Callback<All> {
            override fun onResponse(call: Call<All>, response: Response<All>) {
                if (response.isSuccessful) {
                    val it = response.body()!!
                    val cur = CurWeathPat(
                        1,
                        cityName,
                        options.get("lat")!!,
                        options.get("lon")!!,
                        it.current.temp.toInt().toString(),
                        it.current.feels_like.toInt().toString(),
                        it.current.weather[0].icon,
                        it.current.weather[0].main
                    )
                    curDBDao.curInsertRecord(cur)

                    appDBDao.deleteAll()
                    val time = SimpleDateFormat("HH").format(Calendar.getInstance().time).toInt()
                    for (i in 1..24) {
                        val it1 = response.body()?.hourly!![i]
                        val timeStr: String
                        if (time + i >= 24) {
                            if (time + i - 24 < 10) {
                                timeStr = "0" + (time + i - 24).toString() + ":00"
                            } else {
                                timeStr = (time + i - 24).toString() + ":00"
                            }
                        } else {
                            if (time + i < 10) timeStr = "0" + (time + i).toString() + ":00"
                            else timeStr = (time + i).toString() + ":00"
                        }
                        val hourly = Hourly(i, it1.temp, it1.humidity, timeStr, it1.weather[0].icon)
                        insertAllData(hourly)
                    }

                    weekDBDao.deleteAll()
                    for (i in 0..6) {
                        val sdf = SimpleDateFormat("EEEE")
                        val d = Date()
                        val dayOfWeek = sdf.format(d.time + i * 1000 * 60 * 60 * 24)
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        val it1 = response.body()?.daily!![i]
                        weekDBDao.insertRecord(
                            WeekPat(
                                i + 1,
                                dayOfWeek,
                                it1.weather[0].icon,
                                it1.temp.min.toInt().toString(),
                                it1.temp.max.toInt().toString()
                            )
                        )
                    }

                }
            }

            override fun onFailure(call: Call<All>, t: Throwable) {
                //
            }

        })
    }
    fun locale(url: String, options: MutableMap<String, String>) {//чтобы узнать, есть ли название города на местном языке,
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {}
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val str = response.body()?.string().toString()!!.drop(1).dropLast(1)
                val arr = JSONObject(str)
                val local = JSONObject(arr.getString("local_names"))
                var check: String? = null
                val locale: String = Locale.getDefault().country.toLowerCase()
                try {
                    check = local.getString(locale)
                } catch (e: JSONException) {
                }
                if (check != null)
                    handler.post {
                        options.put("exclude", "minutely,daily,alerts,hourly")
                        apiCall(options, local.getString(locale))
                    }
                else handler.post {
                    options.put("exclude", "minutely,daily,alerts,hourly")
                    apiCall(options, local.getString("en"))
                }

            }
        })
    }
}
