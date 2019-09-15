package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.Location
import co.yulu.assignment.database.entity.Photos
import co.yulu.assignment.database.entity.Stats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StatsConverter {

    @TypeConverter
    fun fromStats(stats: Stats): String {
        val gson = Gson()
        val type = object : TypeToken<Stats>() {}.type
        return gson.toJson(stats, type)
    }

    @TypeConverter
    fun toStats(string: String): Stats {
        val gson = Gson()
        val type = object : TypeToken<Stats>() {}.type
        return gson.fromJson(string, type)
    }

}