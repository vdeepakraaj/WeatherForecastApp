package com.deepak.weatherapplication.ui.forecast

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.deepak.weatherapplication.data.model.Forecast
import com.deepak.weatherapplication.data.model.getColor
import com.deepak.weatherapplication.utils.ImageUtils
import com.deepak.weatherforecast.R

class ForeCastAdapter : RecyclerView.Adapter<ForeCastAdapter.ViewHolder>() {

    private var items: List<Forecast> = java.util.ArrayList()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Forecast>) {
        this.items = items
        notifyDataSetChanged()
    }


    //the class is handling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(foreCast: Forecast) {
            val tempMin = itemView.findViewById(R.id.tvTempMin) as TextView
            val tempMax = itemView.findViewById(R.id.tvTempMax) as TextView
            val dayOfWeek = itemView.findViewById(R.id.tvDayOfWeek) as TextView
            val cardView = itemView.findViewById(R.id.cardView) as CardView
            val imageView = itemView.findViewById(R.id.imageViewForecastIcon) as ImageView
            val desc = itemView.findViewById(R.id.tvDesc) as TextView
            tempMin.text = foreCast.tempMin
            tempMax.text = foreCast.tempMax
            dayOfWeek.text = foreCast.day
            desc.text = foreCast.desc
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cardView.setCardBackgroundColor(getColor(foreCast.day))
            }
            ImageUtils.setWeatherIcon(imageView, foreCast.weatherIcon)
        }
    }
}