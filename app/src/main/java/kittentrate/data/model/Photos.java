
package kittentrate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Photos {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("pages")
    private String mPages;
    @SerializedName("perpage")
    private Long mPerpage;
    @SerializedName("photo")
    private List<Photo> mPhoto;
    @SerializedName("total")
    private String mTotal;

    public Long getPage() {
        return mPage;
    }

    public String getPages() {
        return mPages;
    }

    public Long getPerpage() {
        return mPerpage;
    }

    public List<Photo> getPhoto() {
        return mPhoto;
    }

    public String getTotal() {
        return mTotal;
    }

    public static class Builder {

        private Long mPage;
        private String mPages;
        private Long mPerpage;
        private List<Photo> mPhoto;
        private String mTotal;

        public Photos.Builder withPage(Long page) {
            mPage = page;
            return this;
        }

        public Photos.Builder withPages(String pages) {
            mPages = pages;
            return this;
        }

        public Photos.Builder withPerpage(Long perpage) {
            mPerpage = perpage;
            return this;
        }

        public Photos.Builder withPhoto(List<Photo> photo) {
            mPhoto = photo;
            return this;
        }

        public Photos.Builder withTotal(String total) {
            mTotal = total;
            return this;
        }

        public Photos build() {
            Photos Photos = new Photos();
            Photos.mPage = mPage;
            Photos.mPages = mPages;
            Photos.mPerpage = mPerpage;
            Photos.mPhoto = mPhoto;
            Photos.mTotal = mTotal;
            return Photos;
        }

    }

}
