package com.example.weather.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.Repository
import com.example.weather.db.CurWeathPat
import com.example.weather.db.Hourly
import com.example.weather.db.WeekPat
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
    fun apiCall(options:MutableMap<String,String>,cityName: String){
    repository.apiCall(options,cityName)
}
    fun getLastRec(): LiveData<CurWeathPat>{
        return repository.getLastRec()
    }
    fun getCoords():CurWeathPat{
        return repository.getCoords()
    }
}