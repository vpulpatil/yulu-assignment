package co.yulu.assignment.database.entity.typeconverters

import androidx.room.TypeConverter
import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.Category
import co.yulu.assignment.database.entity.Group
import co.yulu.assignment.database.entity.Icon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GroupConverter {

    @TypeConverter
    fun fromGroup(group: Group): String {
        val gson = Gson()
        val type = object : TypeToken<Group>() {}.type
        return gson.toJson(group, type)
    }

    @TypeConverter
    fun fromArrayListGroup(group: List<Group>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Group>>() {}.type
        return gson.toJson(group, type)
    }

    @TypeConverter
    fun toGroupArrayList(string: String): List<Group> {
        val gson = Gson()
        val type = object : TypeToken<List<Group>>() {}.type
        return gson.fromJson(string, type)
    }

    @TypeConverter
    fun toGroup(string: String): Group {
        val gson = Gson()
        val type = object : TypeToken<Group>() {}.type
        return gson.fromJson(string, type)
    }

}