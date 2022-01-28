package com.example.weather.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class Icon(val icon:String)
/*class TypeConverterOwner {
    val gson : Gson = Gson()
    @TypeConverter
    fun stringToSomeObjectList(data: String?) :Icon? {
        if(data == null)return null
        val listType: Type = object : TypeToken<Icon?>() {}.type
        return gson.fromJson<Icon?>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someobject: Icon?): String?
    {
        return gson.toJson(someobject)
    }
}*/