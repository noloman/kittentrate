
package kittentrate.data.repository.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FlickrPhoto {
    @SerializedName("photos")
    private Photos mPhotos;
    @SerializedName("stat")
    private String mStat;

    public Photos getPhotos() {
        return mPhotos;
    }

    public void setPhotos(Photos photos) {
        mPhotos = photos;
    }

    public String getStat() {
        return mStat;
    }

    public void setStat(String stat) {
        mStat = stat;
    }

}
