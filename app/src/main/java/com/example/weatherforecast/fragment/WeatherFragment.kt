@file:Suppress("DEPRECATION")

package com.example.weatherforecast.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
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
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.adapter.RecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        checkLocation()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val viewModel:ViewModelWeather by activityViewModels()

    private var latitude:Double? = 3.132
    private var longitude:Double? = 101.684

    private lateinit var tvLocation:TextView
    private lateinit var tvCurTemp:TextView
    private lateinit var tvRain:TextView
    private lateinit var bottomSheetBehavior:BottomSheetBehavior<*>
    private lateinit var bottomSheet:View
    private lateinit var recyclerViewAdapter:RecyclerViewAdapter
    private lateinit var geocoder:Geocoder
    private lateinit var parentLayout:RelativeLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvLocation = view.findViewById(R.id.tvLocation)
        tvCurTemp = view.findViewById(R.id.tvCurTemp)
        tvRain = view.findViewById(R.id.tvRainProb)
        bottomSheet = view.findViewById(R.id.bottom_sheet)
        parentLayout = view.findViewById(R.id.main_layout)

        //Get initial location
        val addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
        val address = addresses!!.firstOrNull()
        tvLocation.text = "${address?.locality}"
        getWeatherForecast(latitude!!, longitude!!)

        bottomSheetSettings()

    }

    private fun bottomSheetSettings(){
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        parentLayout.setOnClickListener {
            if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        val recycler2 = bottomSheet.findViewById<RecyclerView>(R.id.recyclerBottomSheet)

        recycler2.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            recyclerViewAdapter = RecyclerViewAdapter()
        }
        viewModel.responseBody.observe(viewLifecycleOwner){
            recyclerViewAdapter.setData(it)
            recycler2.adapter = recyclerViewAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getWeatherForecast(latitude: Double, longitude: Double) {
        viewModel.getForecast(
            latitude,
            longitude,
            "temperature_2m,precipitation_probability",
            "true",
            10,
            "auto")

        viewModel.responseBody.observe(viewLifecycleOwner){
            tvCurTemp.text = "${it.currentWeather?.temperature?.roundToInt()}\u00B0"
            tvRain.text = "Chance of Rain : ${viewModel.getCurAvg()}%"
            recyclerViewAdapter.setData(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkLocation(){

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->

            //Get Lat and Long here
            latitude = String.format("%.3f", location.latitude).toDouble()
            longitude = String.format("%.3f",location.longitude).toDouble()

            val addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
            val address = addresses!!.firstOrNull()
            tvLocation.text = "${address?.locality}"
            getWeatherForecast(latitude!!, longitude!!)
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