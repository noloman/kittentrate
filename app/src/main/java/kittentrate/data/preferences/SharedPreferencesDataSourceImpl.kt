package kittentrate.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kittentrate.repository.datasource.SharedPreferencesDataSource
import kittentrate.utils.Constants


/**
 * Copyright 2018 Manuel Lorenzo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class SharedPreferencesDataSourceImpl(val context: Context) : SharedPreferencesDataSource {
    private var sharedPreferences: Observable<SharedPreferences> =
            Observable.fromCallable {
                PreferenceManager.getDefaultSharedPreferences(context)
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    override fun setPreferencesPhotoTag(photoTag: String) {
        Schedulers.io().createWorker().schedule {
            setStringValue(Constants.PHOTO_TAG_KEY, photoTag)
        }
    }


    private fun setStringValue(key: String, value: String?) {
        sharedPreferences
                .subscribeBy { sharedPreferences ->
                    sharedPreferences
                            .edit()
                            .putString(key, value)
                            .apply()
                }
    }

    override fun getPreferencesPhotoTag(): Observable<String> =
            sharedPreferences
                    .map { t: SharedPreferences ->
                        t.getString(Constants.PHOTO_TAG_KEY, Constants.PHOTO_TAG_KITTEN_VALUE)
                    }

}
