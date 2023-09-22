package com.example.polarbear

import android.telecom.Call
import com.example.polarbear.WeatherApp
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("weather")
    fun getWeatherData(
       @Query("q")city:String,
       @Query("appid")appid:String,
       @Query("units")units:String
    ) : List<WeatherApp>
}


