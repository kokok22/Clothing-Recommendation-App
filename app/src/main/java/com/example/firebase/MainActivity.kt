package com.example.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
//테스트
class MainActivity : AppCompatActivity(), LocationListener{

    val PERMISSION_REQUEST_CODE = 2000
    val APP_ID = "01f01504beb8a874eb66df4f8859f80e"
    val UNITS = "metric"
    var temp = ""

    private val storageRef = FirebaseStorage.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 현재 로그인 되어있는지 확인 로그인 되어있지 않으면 로그인 화면으로 넘어가도록
        if (FirebaseAuth.getInstance().currentUser == null) {
            startMyActivity(LoginActivity::class.java)
        }

        getLocationInfo()

        Logoutbutton.setOnClickListener(onClickListener)
        Optionbutton.setOnClickListener(onClickListener)
        Resetbutton.setOnClickListener(onClickListener)

    }

    internal var onClickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.Logoutbutton -> {
                FirebaseAuth.getInstance().signOut()
                startMyActivity(LoginActivity::class.java)
            }
            R.id.Optionbutton -> startMyActivity(OptionActivity::class.java)
            R.id.Resetbutton -> setImage()
        }
    }

    private fun setImage() {
        // 사진 초기화

        val RefList = ArrayList<StorageReference>()

        RefList.add(storageRef.child("10000.bmp"))
        RefList.add(storageRef.child("10001.bmp"))
        RefList.add(storageRef.child("10007.bmp"))
        RefList.add(storageRef.child("10006.bmp"))
        RefList.add(storageRef.child("KakaoTalk_20191107_110103496.jpg"))

        val ONE_MEGABYTE = (1024 * 1024).toLong()

        RefList[0].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            firstImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[1].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            secondImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[2].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            thirdImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[3].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            fourthImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[4].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            fifthImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            startToast("추천 스타일 입니다.")
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

    }

    private fun startMyActivity(c: Class<*>) {
        val intent = Intent(this, c)
        startActivity(intent)
    }

    fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    }

    fun drawCurrentWeather(currentWeather: TotalWeather?){
        with(currentWeather){


            this?.weatherList?.getOrNull(0)?.let{
                val glide = Glide.with(this@MainActivity)
                glide.load(Uri.parse("https://openweathermap.org/img/wn/"+it.icon +"@2x.png"))
                        .into(weather_now)
            }

            this?.main?.temp?.let{now_temp.text = it.toString()+"°C"}
            this?.main?.tempMax?.let{temp = it.toString()}
            this?.main?.tempMin?.let{tempss.text = temp+"/"+it.toString()}
        }
    }

    fun getLocationInfo(){
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                    this@MainActivity,
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

    fun requestWeatherInfoLocation(latitude: Double, longitude: Double){
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