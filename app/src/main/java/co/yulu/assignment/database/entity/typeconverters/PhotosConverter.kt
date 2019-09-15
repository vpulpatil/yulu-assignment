package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.Location
import co.yulu.assignment.database.entity.Photos
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PhotosConverter {

    @TypeConverter
    fun fromPhotos(photos: Photos?): String {
        val gson = Gson()
        val type = object : TypeToken<Photos>() {}.type
        return gson.toJson(photos, type)
    }

    @TypeConverter
    fun toPhotos(string: String): Photos {
        val gson = Gson()
        val type = object : TypeToken<Photos>() {}.type
        return gson.fromJson(string, type)
    }

}