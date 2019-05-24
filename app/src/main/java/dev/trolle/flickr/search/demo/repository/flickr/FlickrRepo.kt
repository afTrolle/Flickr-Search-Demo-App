package dev.trolle.flickr.search.demo.repository.flickr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * FlickrRepo,
 *
 * Idea is to be able to abstract api implementation for the view models and also add caching later on.
 *
 * https://developer.android.com/jetpack/docs/guide
 *
 */
class FlickrRepo @Inject constructor(
    val flickrSource: FlickrSource
) {
    /*
    * Access point for viewModel to get information from Flickr
    * Can be cached or fetched.
    *
    * */
    data class SearchQuery(
        val queryText: String,
        val sortedBy: Int,
        val licence: String? = null
    )

    /**
     * Perform search based on query
     *
     * @returns live data object will be set with result when it's done
     **/
    fun searchPhoto(searchQuery: SearchQuery): LiveData<MySearchPhotoResponse> {
        val ret = MutableLiveData<MySearchPhotoResponse>()
        GlobalScope.launch(context = Dispatchers.IO) {
            val result = flickrSource.searchPhotos(
                searchQuery.queryText,
                searchQuery.sortedBy,
                searchQuery.licence
            )
            ret.postValue(result)
        }
        return ret
    }

    /**
     * Get photo data of its id
     *
     * @returns live data object with list can only be one item or error
     *
     **/
    fun getPhoto(photoId: String): MutableLiveData<MySearchPhotoResponse> {
        val ret = MutableLiveData<MySearchPhotoResponse>()
        GlobalScope.launch(context = Dispatchers.IO) {
            //blocking function
            val result = flickrSource.getPhoto(photoId)
            ret.postValue(result) //post important we aren't on ui thread
        }
        return ret
    }

    /**
     * Get info about photo of its id
     *
     * @returns live data object with list can only be one item or error
     *
     **/
    fun getPhotoInfo(photoId: String): LiveData<MyInfoPhotoResponse> {
        val ret = MutableLiveData<MyInfoPhotoResponse>()
        GlobalScope.launch(context = Dispatchers.IO) {
            //blocking function
            val result = flickrSource.getPhotoInfo(photoId)
            ret.postValue(result) //post important we aren't on ui thread
        }
        return ret
    }


}