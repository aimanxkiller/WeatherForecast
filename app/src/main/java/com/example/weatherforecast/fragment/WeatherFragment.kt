@file:Suppress("DEPRECATION")

package com.example.weatherforecast.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherforecast.R
import com.example.weatherforecast.viewmodel.ViewModelWeather
import dagger.hilt.android.AndroidEntryPoint
import android.location.Geocoder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.adapter.RecyclerViewAdapter
import java.util.*

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkLocation()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val viewModel:ViewModelWeather by activityViewModels()

    private var latitude:Double? = null
    private var longitude:Double? = null

    private lateinit var tvLocation:TextView
    private lateinit var tvCurTemp:TextView
    private lateinit var tvRain:TextView
    private lateinit var btnTest:Button
    private lateinit var recycler:RecyclerView
    private lateinit var recyclerViewAdapter:RecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvLocation = view.findViewById(R.id.tvLocation)
        tvCurTemp = view.findViewById(R.id.tvCurTemp)
        tvRain = view.findViewById(R.id.tvRainProb)
        btnTest = view.findViewById(R.id.testGet)
        recycler = view.findViewById(R.id.recyclerMain)


        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            recyclerViewAdapter = RecyclerViewAdapter()
        }

        btnTest.setOnClickListener {
            val addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
            val address = addresses!!.firstOrNull()
            tvLocation.text = "${address?.locality}"
            getWeatherForecast(latitude!!, longitude!!)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getWeatherForecast(latitude: Double, longitude: Double) {

        viewModel.getForecast(
            latitude,
            longitude,
            "temperature_2m,precipitation_probability",
            "true",
            7,
            "auto")



        viewModel.responseBody.observe(viewLifecycleOwner){
            tvCurTemp.text = "${it.currentWeather?.temperature.toString()}\u00B0"
            tvRain.text = "Chance of Rain : ${viewModel.getCurAvg()}"

            recyclerViewAdapter.setData(it)
            recycler.adapter = recyclerViewAdapter
        }

    }

    @SuppressLint("SetTextI18n")
    private fun checkLocation(){

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->

            //Get Lat and Long here
            latitude = String.format("%.3f", location.latitude).toDouble()
            longitude = String.format("%.3f",location.longitude).toDouble()

        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
    }

}