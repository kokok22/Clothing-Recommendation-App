package com.example.firebase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt
import android.util.Log

//테스트
class MainActivity : BaseActivity(), LocationListener{

    val PERMISSION_REQUEST_CODE = 2000
    val APP_ID = "01f01504beb8a874eb66df4f8859f80e"
    val UNITS = "metric"
    var temp = ""
    var num = 0
    val random = Random()

    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 현재 로그인 되어있는지 확인 로그인 되어있지 않으면 로그인 화면으로 넘어가도록
        if (FirebaseAuth.getInstance().currentUser == null) {
            finish()
            startMyActivity(LoginActivity::class.java)
        }

        getLocationInfo()

        // 모델이 완성되면 지워질 예정
        setImage()

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

        RefList.add(storageRef.child((224+num).toString() + ".bmp"))
        RefList.add(storageRef.child((221+num).toString() + ".bmp"))
        RefList.add(storageRef.child((250+num).toString() + ".bmp"))
        RefList.add(storageRef.child((229+num).toString() + ".bmp"))
        RefList.add(storageRef.child((230+num).toString() + ".bmp"))

        num += 1

        val ONE_MEGABYTE = (1024 * 1024).toLong()

        RefList[0].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            firstImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            like1.text = "  "+random.nextInt(3000).toString()
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[1].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            secondImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            like2.text = "  "+random.nextInt(3000).toString()
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[2].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            thirdImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            like3.text = "  "+random.nextInt(3000).toString()
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[3].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            fourthImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            like4.text = "  "+random.nextInt(3000).toString()
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

        RefList[4].getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            // 이미지 다운로드 성공
            fifthImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            like5.text = "  "+random.nextInt(3000).toString()
            startToast("추천 스타일 입니다.")
        }.addOnFailureListener {
            // 다운로드 실패
            startToast("fail...")
        }

    }

    private fun startMyActivity(c: Class<*>) {
        val intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (c == LoginActivity::class.java) {
            finish()
        }
        startActivity(intent)
    }

    fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    }

    @SuppressLint("SetTextI18n")
    fun drawCurrentWeather(currentWeather: TotalWeather?){
        with(currentWeather){


            this?.weatherList?.getOrNull(0)?.let{
              /*  val glide = Glide.with(this@MainActivity)
                glide.load(Uri.parse("https://openweathermap.org/img/wn/"+it.icon +"@2x.png"))
                        .into(weather_now)*/
                val Wname:String = it.icon + ".png"
                val assetsBitmap:Bitmap? = getBitmapFromAssets(Wname)
                weather_now.setImageBitmap(assetsBitmap)
            }


            this?.main?.temp?.let{now_temp.text = it.roundToInt().toString()+"°"}
            this?.main?.tempMax?.let{temp = it.roundToInt().toString()}
            this?.main?.tempMin?.let{tempss.text = temp+" / "+it.roundToInt().toString()+"    "}
        }
    }

    private fun getBitmapFromAssets(fileName: String):Bitmap?{
        return try{
            BitmapFactory.decodeStream(assets.open(fileName))
        }catch (e: IOException){
            e.printStackTrace()
            null
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

    fun setLocation(latitude: Double, longitude: Double){
        val geocoder = Geocoder(this)
            var list: List<Address>? = null

            try {
                list = geocoder.getFromLocation(
                        latitude,
                        longitude,
                        10
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

            LocText.text = list!!.get(0).locality
            Log.v("location", list.get(0).locality)
        }

    fun requestWeatherInfoLocation(latitude: Double, longitude: Double){
        setLocation(latitude, longitude)
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