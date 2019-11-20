package com.example.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherActivity : AppCompatActivity(), LocationListener{

    private val PERMISSION_REQUEST_CODE = 2000
    private val APP_ID = "01f01504beb8a874eb66df4f8859f80e"
    private  val UNITS = "metric"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        getLocationInfo()
    }
// http://openweathermap.org/img/wn/10d@2x.png
    private fun drawCurrentWeather(currentWeather: TotalWeather?){
        with(currentWeather){

            this?.weatherList?.getOrNull(0)?.let{
                val glide = Glide.with(this@OpenWeatherActivity)
                glide.load(Uri.parse("https://openweathermap.org/img/wn/"+it.icon +"@2x.png"))
                        .into(current_icon)
            }

            this?.main?.temp?.let{current_now.text = it.toString()}
            this?.main?.tempMax?.let{current_max.text = it.toString()}
            this?.main?.tempMin?.let{current_min.text = it.toString()}
        }
    }


    private fun getLocationInfo(){
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                        this@OpenWeatherActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                    this@OpenWeatherActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE
            )
        } else{
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if(location!=null){
                val latitude = location.latitude
                val longitude = location.longitude
                requestWeatherInfoLocation(latitude = latitude, longitude = longitude)
            }else {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        3000L,
                        0F,
                        this

                )
                locationManager.removeUpdates(this)
            }
        }
    }

    private fun requestWeatherInfoLocation(latitude: Double, longitude: Double){
        (application as WeatherApplication)
                .requestService()
                ?.getWeatherInfoOfCoordinates(
                        latitude = latitude,
                        longitude = longitude,
                        appID = APP_ID,
                        units = UNITS
                )
                ?.enqueue(object : Callback<TotalWeather>{
                    override fun onFailure(call: Call<TotalWeather>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<TotalWeather>, response: Response<TotalWeather>) {
                        val totalWeather = response.body()
                        drawCurrentWeather(totalWeather)
                    }
                })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==PERMISSION_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK) getLocationInfo()
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longtitude = location.longitude
        requestWeatherInfoLocation(latitude = latitude,longitude = longtitude)
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }


}