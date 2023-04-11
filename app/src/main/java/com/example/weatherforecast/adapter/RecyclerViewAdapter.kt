package com.example.weatherforecast.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.ResponseWeather
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapter:RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private lateinit var item:ResponseWeather

    fun setData(responseWeather: ResponseWeather) {
        this.item = responseWeather
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.rows_item,parent,false)
        return MyViewHolder(inflater)
    }

    //setting data values here
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val date:String
        val avg: Double

        when(position){
            0 -> {
                date = "Today"
                avg = calAvg(position)
            }
            1 -> {
                date = "Tomorrow"
                avg = calAvg(position)
            }
            else -> {
                date = getDate(position)
                avg = calAvg(position)
            }
        }

        holder.bind(date,avg)
    }

    override fun getItemCount(): Int {
        return (item.hourly!!.time!!.size/24)
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val date:TextView = itemView.findViewById(R.id.tvDate)
        private val temp:TextView = itemView.findViewById(R.id.tvTemp)

        fun bind(datePass: String, avg: Double) {
            date.text = datePass
            temp.text = "${avg}°"
        }

    }

    private fun calAvg(pos:Int):Double{
        var sum = 0.0
        for(i in (pos*24) until (pos+1)*24){
            sum += item.hourly!!.temperature2m!![i]!!
        }
        return String.format("%.3f", sum/24).toDouble()
    }

    private fun getDate(position: Int): String {

        val isoString = item.hourly!!.time!![position*24] as String

        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
        val date = isoFormat.parse(isoString)

        val monthDateFormat = SimpleDateFormat("MMM dd", Locale.US)

        return monthDateFormat.format(date as Date)
    }

}