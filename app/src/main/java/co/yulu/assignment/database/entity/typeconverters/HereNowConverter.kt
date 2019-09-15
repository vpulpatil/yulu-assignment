package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.HereNow
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HereNowConverter {

    @TypeConverter
    fun fromHereNow(hereNow: HereNow): String {
        val gson = Gson()
        val type = object : TypeToken<HereNow>() {}.type
        return gson.toJson(hereNow, type)
    }

    @TypeConverter
    fun toHereNow(string: String): HereNow {
        val gson = Gson()
        val type = object : TypeToken<HereNow>() {}.type
        return gson.fromJson(string, type)
    }

}