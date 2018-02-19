package kittentrate.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.lang.UnsupportedOperationException

object ReactiveSharedPreferences {
    fun defaultPrefs(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    private fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { editor -> editor.putString(key, value) }
            is Boolean? -> edit { editor -> editor.putBoolean(key, value as Boolean) }
            is Int? -> edit { editor -> editor.putInt(key, value as Int) }
            is Float? -> edit { editor -> editor.putFloat(key, value as Float) }
            is Long? -> edit { editor -> editor.putLong(key, value as Long) }
            else -> throw UnsupportedOperationException("Unsupported operation")
        }
    }

    inline operator fun <reified T> SharedPreferences.get(key: String, defaultValue: T? = null):
            Observable<T> {
        return when (T::class) {
            String::class -> {
                Observable
                        .fromCallable {
                            getString(key, defaultValue as String) as T
                        }
                        .observeOn(Schedulers.io())
            }
            Long::class -> {
                Observable.fromCallable {
                    getLong(key, defaultValue as? Long ?: -1L) as T
                }
                        .observeOn(Schedulers.io())
            }
            Int::class -> {
                Observable.fromCallable {
                    getInt(key, defaultValue as? Int ?: -1) as T
                }
                        .observeOn(Schedulers.io())
            }
            Boolean::class -> {
                Observable.fromCallable {
                    getBoolean(key, defaultValue as? Boolean ?: false) as T
                }
                        .observeOn(Schedulers.io())
            }
            Float::class -> {
                Observable.fromCallable {
                    getFloat(key, defaultValue as? Float ?: -1f) as T
                }
                        .observeOn(Schedulers.io())
            }
            else -> throw UnsupportedOperationException("Unsupported operation")
        }
    }
}