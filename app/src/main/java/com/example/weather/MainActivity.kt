package com.example.weather


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.illabo.dadatasuggestions.model.SuggestionData
import com.example.weather.adapter.HourAdapter
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.db.Hourly
import com.example.weather.db.WeekPat
import com.example.weather.viemodel.MainViewModel
import com.example.weather.adapter.WeekAdapter
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val placesKey = BuildConfig.MAPS_API_KEY
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100
    private var locationPermissionGranted = false
    lateinit var bind: ActivityMainBinding
    var kok: Location? = null
    var latCur = "50"
    var lonCur = "50"
    val city: MutableLiveData<SuggestionData> = MutableLiveData()
    lateinit var mViewModel: MainViewModel
    private val client = OkHttpClient()
    private lateinit var recyclerViewAdapter: HourAdapter
    private lateinit var recViewWeekAdapter: WeekAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)//вызывает метод суперкласса AppCompatActivity()
        bind = ActivityMainBinding.inflate(layoutInflater)//Binding
        setContentView(bind.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        mViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java) //инициализируем объект MainViewModel
        bind.recHour.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewAdapter = HourAdapter()
            adapter = recyclerViewAdapter
        }
        bind.recWeek.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            recViewWeekAdapter = WeekAdapter()
            adapter = recViewWeekAdapter
        }
        mViewModel.getAllData().observe(this@MainActivity, Observer<List<Hourly>> {
            recyclerViewAdapter.setListData(it)
            recyclerViewAdapter.notifyDataSetChanged()
        })
        mViewModel.getAllDataWeek().observe(this@MainActivity, Observer<List<WeekPat>> {
            recViewWeekAdapter.setListData(it)
            recViewWeekAdapter.notifyDataSetChanged()
        })
        mViewModel.getLastRec().observe(this@MainActivity) {
            if (it != null) {
                bind.curTemp.text = it.temp + "°"
                bind.curFeels.text = it.feels_like + "°"
                bind.cityTxt.text = it.cityName
                if (it.icon == "13n" || it.icon == "13d") {
                    bind.curImgIcon.setImageResource(R.drawable.snowflake)
                } else
                    if (it.icon == "01n") bind.curImgIcon.setImageResource(R.drawable.moon)
                    else Picasso.get().load("http://openweathermap.org/img/wn/${it.icon}@2x.png")
                        .into(bind.curImgIcon)
                val main = it.main
                bind.wethView.angle = 20
                bind.wethView.speed = 1000
                bind.wethView.emissionRate = 500F
                bind.wethView.fadeOutPercent = 100F
                if (main == "Snow") {
                    bind.wethView.setWeatherData(PrecipType.SNOW)
                    bind.colImg.setImageResource(R.drawable.snow)
                } else if (main == "Rain") {
                    bind.wethView.setWeatherData(PrecipType.RAIN)
                    bind.colImg.setImageResource(R.drawable.rain2)
                } else if (main == "Clear") {
                    bind.wethView.setWeatherData(PrecipType.CLEAR)
                    bind.colImg.setImageResource(R.drawable.clear)
                } else {
                    bind.wethView.setWeatherData(PrecipType.CLEAR)
                    bind.colImg.setImageResource(R.drawable.clouds)
                }
            }
        }
        city.observe(this) {
            lonCur = it.geo_lon.toString()
            latCur = it.geo_lat.toString()
            CoroutineScope(Dispatchers.IO).launch {
                mViewModel.apiCall(options(), it.city.toString())
            }
        }
        Places.initialize(applicationContext, placesKey)
        val placesClient: PlacesClient = Places.createClient(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val permissions: Array<String> = arrayOf(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

        ActivityCompat.requestPermissions(this@MainActivity, permissions, 123)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Разрешение не предоставлено", Toast.LENGTH_LONG).show()
        }
        val lm =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager //подключаем менеджер локаций
        val isGPSEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        // проверяем что GPS включен
        val cor = mViewModel.getCoords()
        if (cor == null) {
            if (isGPSEnabled) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 10f, this)
                kok = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (kok != null) {
                    latCur = kok?.latitude.toString()
                    lonCur = kok?.longitude.toString()
                    setting()
                } else {
                    val top = mViewModel.getCoords()
                    if (top != null) {
                        latCur = top.lat
                        lonCur = top.lon
                        setting()
                    }
                }

            } else {
                Toast.makeText(this, "Пожалуйста, включите GPS! =)", Toast.LENGTH_LONG).show()
            }
        } else {
            lonCur = cor.lon
            latCur = cor.lat
            CoroutineScope(Dispatchers.IO).launch {
                mViewModel.apiCall(options(), cor.cityName)
            }
        }

        bind.searchCityBtn.setOnClickListener() {
            supportFragmentManager.beginTransaction().replace(R.id.constraint, CityFragment(city))
                .commit()
        }

        bind.backBtn.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Разрешение не предоставлено", Toast.LENGTH_LONG).show()
            }
            val lm =
                this.getSystemService(Context.LOCATION_SERVICE) as LocationManager //подключаем менеджер локаций
            val isGPSEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnabled) {

                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 10f, this)
                kok = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (kok != null) {
                    latCur = kok?.latitude.toString()
                    lonCur = kok?.longitude.toString()
                    setting()
                } else {

                }

            } else {
                Toast.makeText(this, "Пожалуйста, включите GPS! =)", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun options(): MutableMap<String, String> {
        val options: MutableMap<String, String> = HashMap()
        options.put("lat", latCur)
        options.put("lon", lonCur)
        options.put("appid", BuildConfig.OPEN_WEATHER_KEY)
        options.put("units", "metric")
        options.put("lang", "ru")
        return options
    }

   fun setting() {

        CoroutineScope(Dispatchers.IO).launch {
            mViewModel.locale("http://api.openweathermap.org/geo/1.0/reverse?lat=$latCur&lon=$lonCur&limit=1&appid=${BuildConfig.OPEN_WEATHER_KEY}&lang=ru",
                options())
        }
    }
}
