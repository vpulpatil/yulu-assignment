package co.yulu.assignment.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class SharedPreferenceUtils {

    private fun getSharedPreferencesObject(context: Context): SharedPreferences {
        return context.getSharedPreferences(SharedPreferenceConstants.SP_FILE_KEY, Context.MODE_PRIVATE)
    }

    fun getLongFromSharedPreference(context: Context, key: String): Long {
        return getSharedPreferencesObject(context).getLong(key, 0)
    }

    fun getIntFromSharedPreference(context: Context, key: String): Int {
        return getSharedPreferencesObject(context).getInt(key, 0)
    }

    fun getStringFromSharedPreference(context: Context, key: String): String {
        return getSharedPreferencesObject(context).getString(key, "0") ?: "0"
    }

    fun getBooleanFromSharedPreference(context: Context, key: String): Boolean {
        return getSharedPreferencesObject(context).getBoolean(key, false)
    }

    fun getBooleanFromSharedPreference(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getSharedPreferencesObject(context).getBoolean(key, defaultValue)
    }

    @SuppressLint("ApplySharedPref")
    fun <T> putInSharedPreference(context: Context, key: String, t: T) {
        val sharedPref = context.getSharedPreferences(SharedPreferenceConstants.SP_FILE_KEY, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        if (t is String) {
            editor.putString(key, t as String)
        } else if (t is Long) {
            editor.putLong(key, t as Long)
        } else if (t is Int) {
            editor.putInt(key, t as Int)
        } else if (t is Boolean) {
            editor.putBoolean(key, t as Boolean)
        } else if (t is Float) {
            editor.putFloat(key, t as Float)
        }
        editor.commit()
    }

}
