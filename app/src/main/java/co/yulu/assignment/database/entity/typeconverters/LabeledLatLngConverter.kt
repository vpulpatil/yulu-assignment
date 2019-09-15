package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LabeledLatLngConverter {

    @TypeConverter
    fun fromLabeledLatLng(labeledLatLng: LabeledLatLng): String {
        val gson = Gson()
        val type = object : TypeToken<LabeledLatLng>() {}.type
        return gson.toJson(labeledLatLng, type)
    }

    @TypeConverter
    fun fromArrayListLabeledLatLng(labeledLatLng: List<LabeledLatLng>): String {
        val gson = Gson()
        val type = object : TypeToken<List<LabeledLatLng>>() {}.type
        return gson.toJson(labeledLatLng, type)
    }

    @TypeConverter
    fun toLabeledLatLngArrayList(string: String): List<LabeledLatLng> {
        val gson = Gson()
        val type = object : TypeToken<List<LabeledLatLng>>() {}.type
        return gson.fromJson(string, type)
    }

    @TypeConverter
    fun toLabeledLatLng(string: String): LabeledLatLng {
        val gson = Gson()
        val type = object : TypeToken<LabeledLatLng>() {}.type
        return gson.fromJson(string, type)
    }

}