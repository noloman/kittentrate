package kittentrate.data.preferences

import android.content.Context
import android.preference.PreferenceManager
import kittentrate.repository.datasource.SharedPreferencesDataSource
import kittentrate.utils.Constants


/**
 * Created by Manuel Lorenzo on 10/05/2017.
 */

class SharedPreferencesDataSourceImpl(val context: Context) : SharedPreferencesDataSource {
    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun setPreferencesPhotoTag(photoTag: String) {
        setStringValue(Constants.PHOTO_TAG_KEY, photoTag)
    }


    private fun setStringValue(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getPreferencesPhotoTag(): String =
            sharedPreferences.getString(Constants.PHOTO_TAG_KEY, Constants.PHOTO_TAG_KITTEN_VALUE)
}
