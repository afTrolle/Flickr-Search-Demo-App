package dev.trolle.flickr.search.demo.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import androidx.navigation.fragment.navArgs
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.extensions.observe
import dev.trolle.flickr.search.demo.view.application.injector
import dev.trolle.flickr.search.demo.viewModel.fragment.SearchViewModel
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class PhotoDetailsFragment : Fragment() {

    val args: PhotoDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        injector.inject(this)
        return inflater.inflate(R.layout.fragment_photo_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.getPhoto(args.photoId).observe(this) {


        }
    }

}
