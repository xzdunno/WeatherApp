package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.db.WeekPat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.week_row.view.*

class WeekAdapter() : RecyclerView.Adapter<CustomViewHolderWeek>() {
    //var DaysOfWeek=
    private var listData: List<WeekPat>? = null
    fun setListData(listData: List<WeekPat>?) {
        this.listData = listData
    }

    override fun getItemCount(): Int {
        if (listData == null) return 0
        return listData?.size!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderWeek {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.week_row, parent, false)
        return CustomViewHolderWeek(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolderWeek, position: Int) {
        holder.bind(listData?.get(position)!!)
    }

}

class CustomViewHolderWeek(var view: View) : RecyclerView.ViewHolder(view) {
    val weekDayTxt = view.weekDayTxt
    val weekIcon = view.weekIcon
    val weekTempDayTxt = view.weekTempDayTxt
    val weekTempNightTxt = view.weekTempNightTxt
    fun bind(week: WeekPat) {
        if (week.icon == "13n" || week.icon == "13d") {
            weekIcon.setImageResource(R.drawable.snowflake)
        } else
            if (week.icon == "01n") weekIcon.setImageResource(R.drawable.moon)
            else Picasso.get().load("http://openweathermap.org/img/wn/${week.icon}@2x.png")
                .into(weekIcon)
        weekTempDayTxt.text = week.maxTemp + "°"
        weekTempNightTxt.text = week.minTemp + "°"
        weekDayTxt.text = week.dayOfWeek
    }
}