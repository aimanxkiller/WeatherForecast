@file:Suppress("DEPRECATION")

package com.example.weatherforecast.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.adapter.RecyclerViewAdapter
import com.example.weatherforecast.databinding.FragmentWeatherBinding
import com.example.weatherforecast.viewmodel.ViewModelWeather
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private lateinit var binding:FragmentWeatherBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): RelativeLayout {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater,container,false)

        return binding.root
    }

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val viewModel:ViewModelWeather by activityViewModels()

    private var latitude:Double? = 0.0
    private var longitude:Double? = 0.0

    private lateinit var tvLocation:TextView
    private lateinit var tvCurTemp:TextView
    private lateinit var tvRain:TextView
    private lateinit var bottomSheetBehavior:BottomSheetBehavior<*>
    private lateinit var bottomSheet:View
    private lateinit var recyclerViewAdapter:RecyclerViewAdapter
    private lateinit var geocoder:Geocoder
    private lateinit var motion:MotionLayout
    private lateinit var pBar:ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvLocation = binding.tvLocation
        tvCurTemp = binding.tvCurTemp
        tvRain = binding.tvRainProb
        bottomSheet = binding.botSheet
        motion = binding.mainLayout
        pBar = binding.progressBar2

        geocoder = Geocoder(requireContext(), Locale.getDefault())

        checkLocation()
        bottomSheetSettings()
    }

    private fun bottomSheetSettings(){
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.findViewById(R.id.bottom_sheet))

        bottomSheetBehavior.addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //do nothing
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //progress for top view
                motion.progress = slideOffset
            }
        })

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
    private fun getWeatherForecast(latitude: Double?, longitude: Double?) {

        viewModel.getForecast(
            latitude,
            longitude,
            "temperature_2m,precipitation_probability",
            "true",
            null,
            "auto")

        viewModel.responseBody.observe(viewLifecycleOwner){
            tvCurTemp.text = "${it.currentWeather?.temperature?.roundToInt()}\u00B0"
            tvRain.text = "Chance of Rain : ${viewModel.getCurAvg()}%"
            recyclerViewAdapter.setData(it)
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(viewLifecycleOwner){
            when(it){
                true ->{
                    pBar.visibility = View.VISIBLE
                }
                false ->{
                    pBar.visibility = View.GONE
                }
            }
        }
    }

    var locationObtained = false

    @SuppressLint("SetTextI18n")
    private fun checkLocation(){

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            //Get Lat and Long here
            latitude = String.format("%.3f", location.latitude).toDouble()
            longitude = String.format("%.3f",location.longitude).toDouble()

            val addresses = geocoder.getFromLocation(latitude as Double, longitude as Double, 1)
            val address = addresses?.firstOrNull()

            tvLocation.text = "${address?.locality}"

            if (!locationObtained){
                tvLocation.text = "${address?.locality}"

                getWeatherForecast(latitude, longitude)
                locationObtained = true
            }
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