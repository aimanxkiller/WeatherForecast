package com.example.weatherforecast.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherforecast.R
import dagger.hilt.android.AndroidEntryPoint

//Do weather app similar to https://dribbble.com/shots/17288933-Kawaii-Weather-App-Exploration
//API Link as follows "https://api.open-meteo.com/v1/forecast?latitude=3.14&longitude=101.69&hourly=temperature_2m,precipitation_probability&current_weather=true&forecast_days=7&timezone=auto"
//Location by Lat+Long (3 decimal place) , forecast_day should be a variable

//MVVM,HILT,ViewModel, Nav Comp with 1 Fragment only
//Handle error when no lat is entered and show error to user

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLocationPermissions()
    }

    // Check for permission status and request permissions if not granted
    private fun checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    // Override onRequestPermissionsResult to handle permission requests
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do something here

            } else {
                // Permission denied, show a message to the user or handle it in some other way
                Toast.makeText(this,"Permission Denied, App will not work",Toast.LENGTH_SHORT).show()
            }
        }
    }

}