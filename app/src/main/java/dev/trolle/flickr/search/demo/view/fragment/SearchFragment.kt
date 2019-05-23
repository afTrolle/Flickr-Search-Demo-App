package dev.trolle.flickr.search.demo.view.fragment


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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


/**
 * Search Fragment,
 *
 * Let the user search for images, groups, people
 */
class SearchFragment : Fragment() {

    val searchViewModel by viewModel { injector.SearchViewModel }

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
            Snackbar.make(view, it.message, Snackbar.LENGTH_LONG).show()
        }

        recycler_view.layoutManager = GridLayoutManager(context, 2)

     //   val layout = LinearLayoutManager(context)
    //    recycler_view.layoutManager = layout

        recycler_view.adapter = SearchAdapter(
            listOf(MyPhoto.mock(), MyPhoto.mock(), MyPhoto.mock(), MyPhoto.mock())
        ) { photoId ->
            val action = SearchFragmentDirections.actionSearchFragmentToPhotoDetailsFragment(photoId, null)
            findNavController().navigate(action)
        }


        //  set new adapter if new data is available
        searchViewModel.searchResultsLiveData.observe(this) {
            val adapter = SearchAdapter(it) { photoId ->
                Snackbar.make(view, "photo $photoId", Snackbar.LENGTH_LONG).show()
            }
            recycler_view.adapter = adapter
        }

    }
}


