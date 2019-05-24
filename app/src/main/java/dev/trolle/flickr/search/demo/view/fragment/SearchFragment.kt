package dev.trolle.flickr.search.demo.view.fragment


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.dagger.viewModel
import dev.trolle.flickr.search.demo.extensions.observe
import dev.trolle.flickr.search.demo.repository.flickr.MyPhoto
import dev.trolle.flickr.search.demo.view.application.injector
import dev.trolle.flickr.search.demo.view.view.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import androidx.recyclerview.widget.GridLayoutManager
import dev.trolle.flickr.search.demo.dagger.activityViewModel
import dev.trolle.flickr.search.demo.repository.settings.SettingsRepository
import dev.trolle.flickr.search.demo.view.view.SearchGridAdapter


/**
 * Search Fragment,
 *
 * Let the user search for images, groups, people
 */
class SearchFragment : Fragment() {

    val searchViewModel by activityViewModel { injector.SearchViewModel }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Open setting drawer / icon drawer
        filter_button.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }


        //listen to responses of view updates
        searchViewModel.searchLiveData.observe(this) {
            search.queryHint = it.searchHint
        }

        //listen for search input
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.performSearch(it)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        //Perform search when button is clicked
        search_search_button.setOnClickListener {
            search.query.toString().ifEmpty { null }?.let {
                searchViewModel.performSearch(it)
            }
        }

        search.setOnSearchClickListener {
            Snackbar.make(it, "onClick", Snackbar.LENGTH_LONG).show()
        }


        //inflate settings fragment in navigation bar
        val searchPreferenceFragment = PreferenceFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.nav_drawer_frame_layout, searchPreferenceFragment)
            .commit()


        //Show error if fetch failed
        searchViewModel.searchResultsErrorLiveData.observe(this) {
            val activeView = getView() ?: return@observe
            Snackbar.make(activeView, it.message, Snackbar.LENGTH_LONG).show()
        }


        //  Set new adapter if new data is available
        searchViewModel.searchResultsLiveData.observe(this) {

            //called when search item has been clicked
            val onSearchItemClicked = { photoId: String ->
                val action = SearchFragmentDirections.actionSearchFragmentToPhotoDetailsFragment(photoId, null)
                findNavController().navigate(action)
            }

            when (searchViewModel.settingsRepository.getPresentationMode()) {
                //use list adapter and layout
                SettingsRepository.PresentationMode.List -> {
                    recycler_view.layoutManager = LinearLayoutManager(context)
                    recycler_view.adapter = SearchAdapter(it, onSearchItemClicked)
                }

                //use grid adapter and layout
                SettingsRepository.PresentationMode.Grid -> {
                    recycler_view.layoutManager = GridLayoutManager(context, 2)
                    recycler_view.adapter = SearchGridAdapter(it, onSearchItemClicked)
                }
            }
        }

    }
}


