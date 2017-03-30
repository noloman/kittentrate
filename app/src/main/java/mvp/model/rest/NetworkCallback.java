package mvp.model.rest;

import java.util.List;

import mvp.model.entity.PhotoEntity;

/**
 * Created by Manuel Lorenzo
 */

public interface NetworkCallback {
    void onSuccess(List<PhotoEntity> success);

    void onFailure(Throwable t);
}
