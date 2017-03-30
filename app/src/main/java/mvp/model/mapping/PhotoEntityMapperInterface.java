package mvp.model.mapping;

import java.util.Collection;
import java.util.List;

import mvp.model.entity.Photo;
import mvp.model.entity.PhotoEntity;

/**
 * Created by Manuel Lorenzo on 20/03/2017.
 */

public interface PhotoEntityMapperInterface {
    List<PhotoEntity> transform(Collection<Photo> photoCollection);
}
