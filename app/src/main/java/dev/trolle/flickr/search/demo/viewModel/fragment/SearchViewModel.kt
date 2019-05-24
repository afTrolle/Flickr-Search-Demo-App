package dev.trolle.flickr.search.demo.viewModel.fragment

import android.content.Context
import androidx.lifecycle.*
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.repository.flickr.FlickrRepo
import dev.trolle.flickr.search.demo.repository.flickr.MyInfoPhotoResponse
import dev.trolle.flickr.search.demo.repository.flickr.MyPhoto
import dev.trolle.flickr.search.demo.repository.flickr.MySearchPhotoResponse
import dev.trolle.flickr.search.demo.repository.settings.SettingsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val context: Context,
    private val flickrRepo: FlickrRepo,
    val settingsRepository: SettingsRepository
) : ViewModel() {

    data class SearchViewData(val searchHint: String)

    private val searchViewLiveData = MutableLiveData<SearchViewData>()
    val searchLiveData get() = searchViewLiveData as LiveData<SearchViewData>

    val searchResultsLiveData: MediatorLiveData<List<MyPhoto>> = MediatorLiveData()

    val searchResultsErrorLiveData: MutableLiveData<MySearchPhotoResponse.Error> = MutableLiveData()

    init {
        //set Search text hint
        searchViewLiveData.value = SearchViewData(context.getString(R.string.search_hint))

        //push mock data
        //searchResultsLiveData.postValue(listOf(MyPhoto.mock(), MyPhoto.mock(), MyPhoto.mock(), MyPhoto.mock()))

        //refresh layout with same data (will trigger when layout is changed)
        searchResultsLiveData.addSource(settingsRepository.layoutLiveData) {
            searchResultsLiveData.postValue(searchResultsLiveData.value)
        }

    }


    /* View model actions */

    fun performSearch(query: String) {
        val searchQuery = FlickrRepo.SearchQuery(
            queryText = query,
            sortedBy = settingsRepository.getSortedBy(),
            licence = settingsRepository.getLicence()
        )

        //Start request
        val data: LiveData<MySearchPhotoResponse> = flickrRepo.searchPhoto(searchQuery)

        //handle request response
        searchResultsLiveData.addSource(data) {
            when (it) {
                is MySearchPhotoResponse.Success -> searchResultsLiveData.postValue(it.photos)
                is MySearchPhotoResponse.Error -> searchResultsErrorLiveData.postValue(it)
            }

        }
    }

    fun getPhoto(photoId: String): LiveData<MySearchPhotoResponse> {
        return flickrRepo.getPhoto(photoId = photoId)
    }

    fun getPhotoInfo(photoId: String) : LiveData<MyInfoPhotoResponse>  {
        return flickrRepo.getPhotoInfo(photoId)
    }


}

