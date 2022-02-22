package com.example.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.weather.data.Repository
import java.util.function.Consumer

/*private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=100
private var locationPermissionGranted=false
private val placesKey="AIzaSyDQoUDR8fOeriyN5RpwkGyPPuzpABhAffA"
class GPSTracker(  //подключаем менеджер локаций
    var context: Context
) : LocationListener {}
   /* // проверяем что GPS включен
    // проверяем что разрешение получено
    var loc=Pair("50","50")

    val location: Pair<String,String>?
        get() {

            // проверяем что разрешение получено
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "Разрешение не предоставлено", Toast.LENGTH_LONG).show()
                return null
            }
            val lm =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager //подключаем менеджер локаций
            val isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // проверяем что GPS включен
            if (isGPSEnabled) {

                //Log.d("latlon1",kok?.latitude.toString()+" "+kok?.longitude.toString())
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10f, this)
                if(kok==null){
                return loc
                    }
                else return Pair(
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!.latitude.toString(),lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!.longitude.toString())
            } else {
                Toast.makeText(context, "Пожалуйста, включите GPS! =)", Toast.LENGTH_LONG).show()
            }
            return null
        }

    override fun onLocationChanged(location: Location) {
        val repos=Repository()
        loc=Pair(location.latitude.toString(), location.longitude.toString())
Log.d("latLon",location.longitude.toString()+" "+location.latitude.toString())
    }
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}*/*/