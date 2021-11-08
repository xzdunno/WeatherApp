package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.weather.databinding.ActivityMainBinding
import com.github.matteobattilana.weather.PrecipType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var bind:ActivityMainBinding
    lateinit var url1:String
    lateinit var data1:String
    lateinit var lat:String
    lateinit var lon:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.imgIcon.setImageResource(R.drawable.sun2)
        url1 = "https://api.weatherapi.com/v1/forecast.json?key=aa2a2f63bd394f8298b184058210711 &q=Рыбинск&days=1&aqi=no&alerts=no"
        bind.wethView.setWeatherData(PrecipType.RAIN)
        bind.wethView.angle=20
        bind.wethView.speed=1000
        bind.wethView.emissionRate= 500F
        bind.wethView.fadeOutPercent=100F
        /*val job: Job = GlobalScope.launch(Dispatchers.IO) {
            data1 = getUrlData(url1)
            Log.d("вывод", data1)
            getCoords(data1)
            Log.d("вывод", lat+lon)
        }
        if (job.isCompleted)
job.cancel()*/
    }


   private fun getUrlData(url:String):String{
var connection:HttpURLConnection
val reader:BufferedReader
var data= StringBuffer()
val url1=URL(url)
       try{
       connection=url1.openConnection() as HttpURLConnection
       connection.connect()
      val stream: InputStream=connection.inputStream
       reader= BufferedReader(InputStreamReader(stream))
       var line:String?=""
       do{
           line=reader.readLine()
           data.append(line).append("\n")
       }

       while(line!=null)
    connection.disconnect()
    reader.close()}
       catch(e:MalformedURLException){
           e.printStackTrace()}
       catch(e:IOException){
           e.printStackTrace()
       }
           return data.toString()


}
    private fun getCoords(dat:String){
        val obj=JSONObject(dat)
        lat=obj.getJSONObject("location").getDouble("lat").toString()
        lon=obj.getJSONObject("location").getDouble("lon").toString()
    }

}