package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocationConverter {

    @TypeConverter
    fun fromLocation(location: Location): String {
        val gson = Gson()
        val type = object : TypeToken<Location>() {}.type
        return gson.toJson(location, type)
    }

    @TypeConverter
    fun toLocation(string: String): Location {
        val gson = Gson()
        val type = object : TypeToken<Location>() {}.type
        return gson.fromJson(string, type)
    }

}