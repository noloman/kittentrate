package kittentrate.data.rest;

import java.util.List;

import kittentrate.data.repository.model.PhotoEntity;

/**
 * Created by Manuel Lorenzo
 */

public interface NetworkCallback {
    void onSuccess(List<PhotoEntity> success);

    void onFailure(Throwable t);
}
