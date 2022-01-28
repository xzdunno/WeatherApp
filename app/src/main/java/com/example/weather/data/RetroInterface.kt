package com.example.weather.data

import com.example.weather.model.All
import com.example.weather.model.CurrWeath
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetroInterface {
    @GET("data/2.5/onecall?")
    fun getDataHour(@QueryMap options:MutableMap<String,String>): Call<All>
    @GET("data/2.5/onecall?")
    fun getCurWeath(@QueryMap options:MutableMap<String,String>): Call<CurrWeath>
}