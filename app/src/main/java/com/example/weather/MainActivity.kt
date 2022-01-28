package com.example.weather


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.db.Hourly
import com.example.weather.model.Hour
import com.example.weather.viemodel.MainViewModel
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val placesKey="AIzaSyDQoUDR8fOeriyN5RpwkGyPPuzpABhAffA"
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    lateinit var bind:ActivityMainBinding
    lateinit var url2:String
    var city=""
    private val client = OkHttpClient()
     var lat:String=""
     var lon:String=""
    private lateinit var recyclerViewAdapter: HourAdapter
    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        Places.initialize(applicationContext,placesKey)
        val placesClient:PlacesClient=Places.createClient(this)
        val permissions:Array<String> = arrayOf("android.permission.ACCESS_FINE_LOCATION")
        ActivityCompat.requestPermissions(this@MainActivity, permissions, 123)
       // obtieneLocalizacion()
bind.drawdLay.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setting()
        /*bind.fragBtn.setOnClickListener(){
bind.drawdLay.openDrawer(GravityCompat.START)
        }*/
        /*bind.searchCityBtn.setOnClickListener(){

            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = listOf(Place.Field.ID, Place.Field.NAME)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this@MainActivity)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage.toString())
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)*/
    }

    fun setting(){
        val mViewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        bind.recHour.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)

            val decoration  =  DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            recyclerViewAdapter = HourAdapter()
            adapter =recyclerViewAdapter
        }
        url2="https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&exclude=minutely,daily,alerts&lang=ru&units=metric&appid=3797d6452235e4b0ca898b18e28d8ce1"
        var (latCur,lonCur)=mViewModel.getLocation(applicationContext)
    mViewModel.getAllData().observe(this@MainActivity, Observer<List<Hourly>>{
        recyclerViewAdapter.setListData(it)
        recyclerViewAdapter.notifyDataSetChanged()
    } )
        mViewModel.getLastRec().observe(this@MainActivity,{
            if(it!=null){
            bind.curTemp.text=it.temp+"°"
            bind.curFeels.text=it.feels_like+"°"
            bind.cityTxt.text=it.cityName
            Picasso.get().load("http://openweathermap.org/img/wn/${it.icon}@2x.png").into(bind.curImgIcon)
            val main=it.main
            bind.wethView.angle=20
            bind.wethView.speed=1000
            bind.wethView.emissionRate= 500F
            bind.wethView.fadeOutPercent=100F
            if(main=="Snow"){
                bind.wethView.setWeatherData(PrecipType.SNOW)
                bind.colImg.setImageResource(R.drawable.snow)
            }
            else if(main=="Rain"){
                    bind.wethView.setWeatherData(PrecipType.RAIN)
                    bind.colImg.setImageResource(R.drawable.rain2)
                }
                else if(main=="Clear"){
                bind.wethView.setWeatherData(PrecipType.CLEAR)
                bind.colImg.setImageResource(R.drawable.clear)}
                else {
                    bind.wethView.setWeatherData(PrecipType.CLEAR)
                    bind.colImg.setImageResource(R.drawable.clouds)
                }}
        })
        val options:MutableMap<String,String> =HashMap()
        options.put("lat",latCur)
        options.put("lon",lonCur)
        options.put("appid","3797d6452235e4b0ca898b18e28d8ce1")
        options.put("exclude","minutely,daily,alerts")
        options.put("units","metric")
        options.put("lang","ru")
        fun run(url: String,options:MutableMap<String,String>) {
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response){
                    val str= response.body()?.string().toString()!!.drop(1).dropLast(1)
                    val arr=JSONObject(str)
                    val local=JSONObject(arr.getString("local_names"))
                    runOnUiThread { mViewModel.getCurWeath(options, local.getString("ru"))}
                }
            })
        }
        GlobalScope.launch(Dispatchers.IO) {mViewModel.apiCall(options)
            options.put("exclude","minutely,daily,alerts,hourly")
run("http://api.openweathermap.org/geo/1.0/reverse?lat=$latCur&lon=$lonCur&limit=1&appid=3797d6452235e4b0ca898b18e28d8ce1&lang=ru",options)

        }


    }

   }
