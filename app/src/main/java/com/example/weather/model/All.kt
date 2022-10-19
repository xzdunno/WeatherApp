package com.example.weather.model

data class All(val current: Current, val hourly: List<Hour>, val daily: List<Daily>)