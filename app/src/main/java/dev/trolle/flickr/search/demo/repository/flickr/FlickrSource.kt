package dev.trolle.flickr.search.demo.repository.flickr

import android.os.SystemClock
import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.FlickrException
import com.flickr4java.flickr.photos.SearchParameters
import dagger.Reusable
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@Reusable
class FlickrSource @Inject constructor(
    private val flickr: Flickr
) {

    fun searchPhotos(
        text: String,
        sorted: Int = SearchParameters.DATE_POSTED_DESC,
        license: String? = null,
        page: Int = 0
    ): MySearchPhotoResponse = try {
        val searchParameters = SearchParameters()
        searchParameters.text = text
        searchParameters.sort = sorted
        searchParameters.license = license
        flickr.photosInterface.search(searchParameters, 0, page).map { it.toMyPhoto() }.let {
            MySearchPhotoResponse.Success(it)
        }
    } catch (exception: FlickrException) {
        MySearchPhotoResponse.Error(exception.errorMessage, exception.errorCode)
    }

    fun getPhoto(
        id: String
    ): MySearchPhotoResponse = try {
        flickr.photosInterface.getInfo(id, null).toMyPhoto().let {
            MySearchPhotoResponse.Success(listOf(it))
        }
    } catch (exception: FlickrException) {
        MySearchPhotoResponse.Error(exception.errorMessage, exception.errorCode)
    } catch (_: Exception) {
        MySearchPhotoResponse.Error("failed parsing image", "0")
    }

    fun getComments(photoId: String) = flickr.commentsInterface.getList(photoId)


    fun getPhotoInfo(photoId: String): MyInfoPhotoResponse? = try {
        val comments = flickr.commentsInterface.getList(photoId)
        val date = Date(SystemClock.currentThreadTimeMillis())

        val photosMeta = flickr.statsInterface.getPhotoStats(photoId, date).toMyStats()
        MyInfoPhotoResponse.Success(comments, photosMeta)
    } catch (exception: FlickrException) {
        MyInfoPhotoResponse.Error(exception.errorMessage, exception.errorCode)
    }

}

