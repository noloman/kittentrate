package kittentrate.data.mapping

import kittentrate.data.model.Photo
import kittentrate.data.model.PhotoEntity

/**
 * Created by Manuel Lorenzo on 20/03/2017.
 */

interface PhotoEntityMapperInterface {
    fun mapToEntity(photoCollection: List<Photo>): List<PhotoEntity>
}
