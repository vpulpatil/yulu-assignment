package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.Category
import co.yulu.assignment.database.entity.Icon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IconConverter {

    @TypeConverter
    fun fromIcon(icon: Icon): String {
        val gson = Gson()
        val type = object : TypeToken<Icon>() {}.type
        return gson.toJson(icon, type)
    }

    @TypeConverter
    fun toIcon(string: String): Icon {
        val gson = Gson()
        val type = object : TypeToken<Icon>() {}.type
        return gson.fromJson(string, type)
    }

}