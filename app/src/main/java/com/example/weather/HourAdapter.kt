package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.db.Hourly
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.hour_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class HourAdapter(/*val all: MainActivity.all*/):RecyclerView.Adapter<CustomViewHolder>() {
    private var listData: List<Hourly>? = null
    fun setListData(listData: List<Hourly>?) {
        this.listData = listData
    }

    override fun getItemCount():Int{
        //
        if (listData==null) return 0
        return listData?.size!!
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.hour_row,parent,false)
        return CustomViewHolder(view)
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val timeStr:String
        //Log.d("Position",position.toString())
       if(holder.time+position+1>=24){
           if(holder.time+position-23<10) {
            timeStr="0"+(holder.time+position-23).toString()+":00"
           }
           else {timeStr=(holder.time+position-23).toString()+":00"}
       }
       else{
           timeStr=(holder.time+position+1).toString()+":00"}

holder.txtTime.text=timeStr
holder.bind(listData?.get(position)!!)


}}
class CustomViewHolder(var view: View):RecyclerView.ViewHolder(view){
    val txtTemp=view.txtTemp
    val txtHumidity=view.txtHumidity
    val txtTime=view.txtTime
    val imgIcon=view.imgIcon

fun bind(hourly: Hourly){
txtHumidity.text=hourly.humidity+"%"
    txtTemp.text=hourly.temp.toDouble().toInt().toString()+"°"
    if(hourly.picture=="13n"||hourly.picture=="13d"){
        imgIcon.setImageResource(R.drawable.snowflake)
    }
    else
        if(hourly.picture=="01n") imgIcon.setImageResource(R.drawable.moon)
    else Picasso.get().load("http://openweathermap.org/img/wn/${hourly.picture}@2x.png").into(imgIcon)
}
    val time=SimpleDateFormat("HH").format(Calendar.getInstance().time).toInt()
}