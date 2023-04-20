@file:Suppress("DEPRECATION")

package com.example.weatherforecast.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
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
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.adapter.RecyclerViewAdapter
import com.example.weatherforecast.databinding.FragmentWeatherBinding
import com.example.weatherforecast.model.ResponseWeather
import com.example.weatherforecast.model.TemperatureModel
import com.example.weatherforecast.viewmodel.ViewModelRoom
import com.example.weatherforecast.viewmodel.ViewModelWeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


@SuppressLint("SetTextI18n")
@AndroidEntryPoint

// TODO -
// Save location temp and time to room database
// Load previous saved data on boot up
// Then only refresh data once everytime app boots to update

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

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel:ViewModelWeather by activityViewModels()
    private val viewModel2:ViewModelRoom by activityViewModels()

    private var latitude:Double? = 0.0
    private var longitude:Double? = 0.0

    private lateinit var tvLocation:TextView
    private lateinit var tvCurTemp:TextView
    private lateinit var tvRain:TextView
    private lateinit var tvUpdate:TextView
    private lateinit var bottomSheetBehavior:BottomSheetBehavior<*>
    private lateinit var bottomSheet:View
    private lateinit var geocoder:Geocoder
    private lateinit var motion:MotionLayout
    private lateinit var pBar:ProgressBar

    private lateinit var recycler2:RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvLocation = binding.tvLocation
        tvCurTemp = binding.tvCurTemp
        tvRain = binding.tvRainProb
        tvUpdate = binding.tvUpdate
        bottomSheet = binding.botSheet
        motion = binding.mainLayout
        pBar = binding.progressBar2

        recycler2 = bottomSheet.findViewById(R.id.recyclerBottomSheet)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),onBackPressedCallback)
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        motionBehavior()

        loadCache()
    }

    private fun motionBehavior(){
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

        viewModel.loading.observe(viewLifecycleOwner){
            when(it){
                true ->{
                    motion.visibility = View.INVISIBLE
                    pBar.visibility = View.VISIBLE
                }
                false ->{
                    motion.visibility = View.VISIBLE
                    pBar.visibility = View.GONE
                }
            }
        }
    }

    private fun loadCache(){

        lifecycleScope.launch{

            viewModel2.allData.observe(viewLifecycleOwner) {
                val list = arrayListOf<TemperatureModel>()


                it.forEachIndexed { index, tempCache ->
                    val x = TemperatureModel(tempCache.date, tempCache.temp?.toDouble())
                    list.add(x)
                    tvUpdate.text = "Last update :${it[0].update}"
                    tvLocation.text = it[0].location
                    tvRain.text = it[0].rainProb
                    tvCurTemp.text = it[0].curTemp

                    tvUpdate.visibility = View.VISIBLE

                }

                recycler2.apply {
                    adapter = RecyclerViewAdapter(list)
                }
            }

            delay(2500)
            checkLocation()
        }
    }

    private fun bottomSheetSettings(
        response: ResponseWeather,
        curTemp: String,
        rainProb: String
    ){
        val group = response.hourly?.time?.groupBy { it.split("T")[0]}?: mapOf()
        val list = arrayListOf<TemperatureModel>()
        var start=0
        var end=23
        group.forEach {
            val tempList =response.hourly?.temperature2m?.slice(start..end)?: arrayListOf()

            val temp = TemperatureModel(time = getDate(it.key), temperature2m = (tempList.sum()/24))
            list.add(temp)
            start+=24
            end+=24
        }
        recycler2.apply {
            adapter = RecyclerViewAdapter(list)
        }
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy - HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        tvUpdate.text = formattedDate

        viewModel2.insertData(list,formattedDate,tvLocation.text.toString(),curTemp,rainProb)

    }

    private fun getDate(time:String): String {
        if (time.isEmpty()){
            return time
        }
        val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = isoFormat.parse(time)

        val monthDateFormat = SimpleDateFormat("MMM dd", Locale.US)

        return monthDateFormat.format(date as Date)
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
            bottomSheetSettings(it,tvCurTemp.text.toString(),tvRain.text.toString())
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun checkLocation(){
        var locationObtained = false

        locationListener = LocationListener { location ->
            if (!locationObtained){
                locationObtained = true
                locationManager.removeUpdates(locationListener)
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


        //Updated here used fusedLocationClient for better location get
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Use the location
                    latitude = location.latitude
                    longitude = location.longitude

                    val addresses = geocoder.getFromLocation(latitude as Double, longitude as Double, 1)
                    val address = addresses?.firstOrNull()

                    // Do something with latitude and longitude
                    tvLocation.text = "${address?.locality}"
                    getWeatherForecast(latitude, longitude)
                }
            }
            .addOnFailureListener { e ->
                // Handle the error
                Toast.makeText(requireContext(),"Error: $e",Toast.LENGTH_SHORT).show()
            }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button event
            requireActivity().finish()
        }
    }

}