package com.example.weather


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.ActivityMainBinding
import com.github.matteobattilana.weather.PrecipType
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException



class MainActivity : AppCompatActivity() {
    lateinit var bind:ActivityMainBinding
    lateinit var url2:String
    var city="Рыбинск"
     var lat:String="58"
     var lon:String="38"
    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setting()

run(url2)

    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("jfi")
            }
            override fun onResponse(call: Call?, response: Response?){
               val body=response?.body()?.string()
                val gson=GsonBuilder().create()
                val all2=gson.fromJson(body,all::class.java)
                runOnUiThread {
                bind.recHour.adapter=HourAdapter(all2)}

            }
        })
    }
    fun setting(){
        bind.imgIcon.setImageResource(R.drawable.sun2)
        bind.wethView.setWeatherData(PrecipType.RAIN)
        bind.wethView.angle=20
        bind.wethView.speed=1000
        bind.wethView.emissionRate= 500F
        bind.wethView.fadeOutPercent=100F
        bind.recHour.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        url2="https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&exclude=minutely,daily,alerts&lang=ru&units=metric&appid="}
data class all(var hourly:List<Hour>)
data class Hour(val temp:String,val humidity:String,val weather:List<Icon>)
data class Icon(val icon:String)}
