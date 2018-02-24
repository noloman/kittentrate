package kittentrate.data.model

import com.google.gson.annotations.SerializedName

data class FlickrPhoto(
        @SerializedName("photos")
        val photos: Photos,
        @SerializedName("stat")
        val stat: String)