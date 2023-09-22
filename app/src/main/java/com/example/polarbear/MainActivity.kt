package com.example.polarbear

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polarbear.databinding.ActivityMainBinding
import com.google.android.material.search.SearchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchWeatherData()
        SearchCity()
    }
    private fun SearchCity(){
        val searchView=binding.searchView
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?):Boolean{
                if (query != null){
                    fetchWeatherData(query)
                }
                return true

            }

            override fun onQueryTextSubmit(query: String?):Boolean{
                return true

            }

        })
    }
    private fun fetchWeatherData(cityName:String) {
        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response=retrofit.getWeatherData("jaipur", "c4ee9e3609ea8674be47579dc97906e5", "matric")
            response.enqueue(object:Callback<WeatherApp>{

            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
               val responseBody= response.body()
                if (response.isSuccessful && responseBody !=null){
                    val temperature=responseBody.main.temp.toString()
                    binding.temp.text="$temperature °C"

                    val humidity=responseBody.main.humidity

                    val windspeed = responseBody.wind.speed

                    val sunRise=responseBody.sys.sunrise.toLong()

                    val sunSet=responseBody.sys.sunset.toLong()

                    val seaLevel=responseBody.main.pressure

                    val condition= responseBody.weather.firstOrNull()?.main?:"unknown"

                    val maxTemp= responseBody.main.temp_max

                    val minTemp= responseBody.main.temp_min


                    binding.temp.text="$temperature °C"
                    binding.weather.text=condition
                    binding.maxTemp.text="Max Temp: $maxTemp °C"
                    binding.minTemp.text="Min Temp: $minTemp °C"
                    binding.humidity.text="$humidity %"
                    binding.speed.text="$windspeed m/s"
                    binding.sunrise.text="${time(sunRise)}"
                    binding.sunset.text="${time(sunSet)}"
                    binding.sea.text="$seaLevel"
                    binding.conditions.text=condition
                    binding.day.text=dayName(System.currentTimeMillis())
                    binding.date.text=date()
                    binding.cityName.text="$cityName"
                    changeImageAccordingToWeatherCondition()

                }
           }

           override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
               TODO("Not yet implemented")
           }

       })

    }
    //change background without using xml
    private fun changeImageAccordingToWeatherCondition(conditions:String) {
        when(conditions){
            "Clear Sky", "Sunny", "Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)    //use of lottie
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy"->{
                binding.root.setBackgroundResource(R.drawable.cloud_black)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)    //use of lottie
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)    //use of lottie
            }

            "Light snow", "Moderate Snow", "Heavy Snow", "Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)    //use of lottie
            }else->{
            binding.root.setBackgroundResource(R.drawable.sunny_background)
            binding.lottieAnimationView.setAnimation(R.raw.sun)    //use of lottie

            }

        }
        binding.lottieAnimationView.playAnimation()             // lottie animation



    }

    private fun date(): String{
        val sdf=SimpleDateFormat("dd MMMM yyyy", Locate.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long):String{
        val sdf=SimpleDateFormat("HH:mm", Locate.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }



    fun dayName(timestamp: Long):String{
        val sdf=SimpleDateFormat("EEEE", Locate.getDefault())
        return sdf.format((Date()))
    }



}







