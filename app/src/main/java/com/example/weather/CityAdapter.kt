package com.example.weather

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import app.illabo.dadatasuggestions.model.SuggestionData


class CityAdapter(var kok: MutableLiveData<SuggestionData>) :
    RecyclerView.Adapter<CustomViewHolderCity>() {
    private var listData: List<SuggestionData>? = null
    fun setListData(listData: List<SuggestionData>?) {
        this.listData = listData
    }

    override fun getItemCount(): Int {
        Log.d("kok", listData?.size.toString())
        if (listData == null) return 0
        return listData?.size!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderCity {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_row, parent, false)
        return CustomViewHolderCity(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolderCity, position: Int) {
        holder.bind(listData?.get(position)!!)
        holder.txtCity.setOnClickListener {
            kok.value = listData?.get(position)!!
        }

    }
}

class CustomViewHolderCity(view: View) : RecyclerView.ViewHolder(view) {
    val txtCity = view.findViewById<TextView>(R.id.cityTxt)

    fun bind(data: SuggestionData) {
        txtCity.text = data.city
        Log.d("kok", data.city.toString())
    }
}