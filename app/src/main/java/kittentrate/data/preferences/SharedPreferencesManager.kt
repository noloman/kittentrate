package kittentrate.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Manuel Lorenzo on 10/05/2017.
 */

class SharedPreferencesManager(val context: Context) {
    private lateinit var instance: SharedPreferences

    fun setStringValue(key: String, value: String) {
        if (instance == null) {
            instance = PreferenceManager.getDefaultSharedPreferences(context)
        }
        instance.edit().putString(key, value).apply()
    }
}
