package com.example.weather.viemodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
//import com.example.weather.GPSTracker
import com.example.weather.data.Repository
import com.example.weather.db.CurWeathPat
import com.example.weather.db.Hourly
import com.example.weather.db.WeekPat
import com.example.weather.model.CurrWeath
import com.example.weather.model.Hour
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    fun getAllData():LiveData<List<Hourly>>{
        return repository.getAllData()
    }
    fun getAllDataWeek():LiveData<List<WeekPat>>{
        return repository.getAllDataWeek()
    }
    fun apiCall(options:MutableMap<String,String>){
    repository.apiCall(options)
}
    fun getCurWeath(options:MutableMap<String,String>,cityName:String){
        repository.getCurWeath(options,cityName)

    }
    fun getLastRec(): LiveData<CurWeathPat>{
        return repository.getLastRec()
    }
    fun getCoords():CurWeathPat{
        return repository.getCoords()
    }
    fun getWeekWeath(options: MutableMap<String, String>){
        repository.getWeekWeath(options)
    }
}