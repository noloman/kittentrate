package kittentrate.data.model

import com.google.gson.annotations.SerializedName

data class Photos(
        @SerializedName("page")
        val page: Long,
        @SerializedName("pages")
        val pages: String,
        @SerializedName("perpage")
        val perpage: Long,
        @SerializedName("photo")
        val photo: List<Photo>,
        @SerializedName("total")
        val total: String
)
