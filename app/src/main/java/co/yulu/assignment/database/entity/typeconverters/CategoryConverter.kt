package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryConverter {

    @TypeConverter
    fun fromCategory(category: Category): String {
        val gson = Gson()
        val type = object : TypeToken<Category>() {}.type
        return gson.toJson(category, type)
    }

    @TypeConverter
    fun toCategory(string: String): Category {
        val gson = Gson()
        val type = object : TypeToken<Category>() {}.type
        return gson.fromJson(string, type)
    }

    @TypeConverter
    fun fromArrayListCategory(category: List<Category>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.toJson(category, type)
    }

    @TypeConverter
    fun toCategoryArrayList(string: String): List<Category> {
        val gson = Gson()
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(string, type)
    }

}