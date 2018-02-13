package kittentrate.repository.datasource

/**
 * Created by Manuel Lorenzo on 12/02/2018.
 */
interface SharedPreferencesDataSource {
    fun getPreferencesPhotoTag(): String
    fun setPreferencesPhotoTag(photoTag: String)
}