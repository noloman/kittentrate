package kittentrate.data.mapping;

import java.util.Collection;
import java.util.List;

import kittentrate.data.repository.model.Photo;
import kittentrate.data.repository.model.PhotoEntity;

/**
 * Created by Manuel Lorenzo on 20/03/2017.
 */

public interface PhotoEntityMapperInterface {
    List<PhotoEntity> transform(Collection<Photo> photoCollection);
}
