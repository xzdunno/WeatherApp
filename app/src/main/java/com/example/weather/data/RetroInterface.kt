package com.example.weather.data

import com.example.weather.model.All
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetroInterface {
    @GET("data/2.5/onecall?")
    fun getDataHour(@QueryMap options:MutableMap<String,String>): Call<All>

}