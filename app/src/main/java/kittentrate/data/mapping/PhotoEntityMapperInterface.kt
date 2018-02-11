package kittentrate.data.mapping

import kittentrate.data.repository.model.Photo
import kittentrate.data.repository.model.PhotoEntity

/**
 * Created by Manuel Lorenzo on 20/03/2017.
 */

interface PhotoEntityMapperInterface {
    fun transform(photoCollection: List<Photo>): List<PhotoEntity>
}
