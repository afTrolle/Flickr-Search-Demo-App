package dev.trolle.flickr.search.demo.viewModel.fragment

import android.content.Context
import androidx.lifecycle.*
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.repository.flickr.FlickrRepo
import dev.trolle.flickr.search.demo.repository.flickr.MyPhoto
import dev.trolle.flickr.search.demo.repository.flickr.MySearchPhotoResponse
import dev.trolle.flickr.search.demo.repository.settings.SettingsRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val context: Context,
    private val flickrRepo: FlickrRepo,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    data class SearchViewData(val searchHint: String)

    private val searchViewLiveData = MutableLiveData<SearchViewData>()
    val searchLiveData get() = searchViewLiveData as LiveData<SearchViewData>

    val searchResultsLiveData: MediatorLiveData<List<MyPhoto>> = MediatorLiveData()
    val searchResultsErrorLiveData: MutableLiveData<MySearchPhotoResponse.Error> = MutableLiveData()

    init {
        searchViewLiveData.value = SearchViewData("search")
    }

    private val hintExtra = context.getString(R.string.search_for)

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


}

