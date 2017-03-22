package mvp.model.rest;

import java.util.List;

import mvp.model.entity.PhotoEntity;

/**
 * Created by manu on 19/03/2017.
 */

public interface NetworkCallback {
    void onSuccess(List<PhotoEntity> success);

    void onFailure(Throwable t);
}
