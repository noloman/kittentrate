package kittentrate.data.model

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("farm")
        val farm: Long,
        @SerializedName("id")
        val id: String,
        @SerializedName("isfamily")
        val isfamily: Long,
        @SerializedName("isfriend")
        val isfriend: Long,
        @SerializedName("ispublic")
        val ispublic: Long,
        @SerializedName("owner")
        val owner: String,
        @SerializedName("secret")
        val secret: String,
        @SerializedName("server")
        val server: String,
        @SerializedName("title")
        val title: String)