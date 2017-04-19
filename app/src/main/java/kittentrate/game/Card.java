package kittentrate.game;

/**
 * Created by Manuel Lorenzo on 13/03/2017.
 */

public class Card {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String getImageCoverUrl() {
        return imageCoverUrl;
    }

    void setImageCoverUrl(String imageCoverUrl) {
        this.imageCoverUrl = imageCoverUrl;
    }

    private String imageCoverUrl;
}
