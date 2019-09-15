package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BeenHereConverter {

    @TypeConverter
    fun fromBeenHere(beenHere: BeenHere): String {
        val gson = Gson()
        val type = object : TypeToken<BeenHere>() {}.type
        return gson.toJson(beenHere, type)
    }

    @TypeConverter
    fun toBeenHere(string: String): BeenHere {
        val gson = Gson()
        val type = object : TypeToken<BeenHere>() {}.type
        return gson.fromJson(string, type)
    }

}