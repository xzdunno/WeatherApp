package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class HourAdapter(val all: MainActivity.all):RecyclerView.Adapter<CustomViewHolder>() {


    override fun getItemCount():Int{
        //
        return 24
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.hour_row,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


    holder.view.findViewById<TextView>(R.id.txtTemp).text=all.hourly[position+1].temp.toDouble().toInt().toString()+"°"
    holder.view.findViewById<TextView>(R.id.txtHumidity).text=all.hourly[position+1].humidity+"%"
    holder.view.findViewById<TextView>(R.id.txtTime).text=
        (SimpleDateFormat("hh").format(Calendar.getInstance().time).toInt()+position+1).toString()
        Picasso.get().load("http://openweathermap.org/img/wn/${all.hourly[position+1].weather[0].icon}@2x.png").into(holder.view.findViewById<ImageView>(R.id.imgIcon))

    }


}
class CustomViewHolder(var view: View):RecyclerView.ViewHolder(view){

}