package kittentrate.data.preferences

import android.content.Context
import android.preference.PreferenceManager
import kittentrate.data.repository.GameDataSource
import kittentrate.utils.Constants

/**
 * Created by Manuel Lorenzo on 10/05/2017.
 */

class SharedPreferencesManager(val context: Context) : GameDataSource.SharedPreferencesDataSource {
    private val instance by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun setPreferencesPhotoTag(photoTag: String?) {
        setStringValue(Constants.PHOTO_TAG_KEY, photoTag)
    }

    private fun setStringValue(key: String, value: String?) {
        instance.edit().putString(key, value).apply()
    }

    override fun getPreferencesPhotoTag(): String {
        return instance.getString(Constants.PHOTO_TAG_KEY, Constants.PHOTO_TAG_KITTEN_VALUE)
    }
}
