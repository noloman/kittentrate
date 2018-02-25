package kittentrate.data.mapping

import kittentrate.data.model.Photo
import kittentrate.data.model.PhotoEntity
import kittentrate.utils.Constants
import manulorenzo.me.kittentrate.BuildConfig
import java.util.*

/**
 * Created by Manuel Lorenzo on 19/03/2017.
 */

class PhotoEntityMapper : PhotoEntityMapperInterface {
    override fun mapToEntity(photoCollection: List<Photo>): List<PhotoEntity> {
        val photoEntityArrayList = ArrayList<PhotoEntity>()
        var count = 0
        val it = photoCollection.iterator()
        while (it.hasNext() && count < photoCollection.size / Constants.NUMBER_MATCHING_CARDS) {
            ++count
            val photo = it.next()
            val photoUrlString = "https://farm" + photo.farm + ".staticflickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret + ".jpg"
            val photoEntity = PhotoEntity(photo.id, photoUrlString)
            photoEntityArrayList.add(photoEntity)
        }
        val newPhotoEntityArrayList1 = ArrayList<PhotoEntity>()
        for (i in 0 until count / (2 * Constants.NUMBER_MATCHING_CARDS)) {
            newPhotoEntityArrayList1.addAll(photoEntityArrayList)
        }
        if (BuildConfig.BUILD_TYPE.equals("release", ignoreCase = true)) {
            Collections.shuffle(newPhotoEntityArrayList1, Random(System.nanoTime()))
        }
        return newPhotoEntityArrayList1
    }
}
