
package kittentrate.data.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Photo {

    @SerializedName("farm")
    private Long mFarm;
    @SerializedName("id")
    private String mId;
    @SerializedName("isfamily")
    private Long mIsfamily;
    @SerializedName("isfriend")
    private Long mIsfriend;
    @SerializedName("ispublic")
    private Long mIspublic;
    @SerializedName("owner")
    private String mOwner;
    @SerializedName("secret")
    private String mSecret;
    @SerializedName("server")
    private String mServer;
    @SerializedName("title")
    private String mTitle;

    public Long getFarm() {
        return mFarm;
    }

    public String getId() {
        return mId;
    }

    public Long getIsfamily() {
        return mIsfamily;
    }

    public Long getIsfriend() {
        return mIsfriend;
    }

    public Long getIspublic() {
        return mIspublic;
    }

    public String getOwner() {
        return mOwner;
    }

    public String getSecret() {
        return mSecret;
    }

    public String getServer() {
        return mServer;
    }

    public String getTitle() {
        return mTitle;
    }

    public static class Builder {

        private Long mFarm;
        private String mId;
        private Long mIsfamily;
        private Long mIsfriend;
        private Long mIspublic;
        private String mOwner;
        private String mSecret;
        private String mServer;
        private String mTitle;

        public Photo.Builder withFarm(Long farm) {
            mFarm = farm;
            return this;
        }

        public Photo.Builder withId(String id) {
            mId = id;
            return this;
        }

        public Photo.Builder withIsfamily(Long isfamily) {
            mIsfamily = isfamily;
            return this;
        }

        public Photo.Builder withIsfriend(Long isfriend) {
            mIsfriend = isfriend;
            return this;
        }

        public Photo.Builder withIspublic(Long ispublic) {
            mIspublic = ispublic;
            return this;
        }

        public Photo.Builder withOwner(String owner) {
            mOwner = owner;
            return this;
        }

        public Photo.Builder withSecret(String secret) {
            mSecret = secret;
            return this;
        }

        public Photo.Builder withServer(String server) {
            mServer = server;
            return this;
        }

        public Photo.Builder withTitle(String title) {
            mTitle = title;
            return this;
        }

        public Photo build() {
            Photo Photo = new Photo();
            Photo.mFarm = mFarm;
            Photo.mId = mId;
            Photo.mIsfamily = mIsfamily;
            Photo.mIsfriend = mIsfriend;
            Photo.mIspublic = mIspublic;
            Photo.mOwner = mOwner;
            Photo.mSecret = mSecret;
            Photo.mServer = mServer;
            Photo.mTitle = mTitle;
            return Photo;
        }

    }

}
