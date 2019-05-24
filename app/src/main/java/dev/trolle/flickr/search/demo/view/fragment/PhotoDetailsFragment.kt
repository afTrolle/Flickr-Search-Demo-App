package dev.trolle.flickr.search.demo.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.dagger.activityViewModel
import dev.trolle.flickr.search.demo.extensions.observe
import dev.trolle.flickr.search.demo.repository.flickr.MyInfoPhotoResponse
import dev.trolle.flickr.search.demo.repository.flickr.MySearchPhotoResponse
import dev.trolle.flickr.search.demo.view.application.injector
import dev.trolle.flickr.search.demo.viewModel.fragment.SearchViewModel
import kotlinx.android.synthetic.main.fragment_photo_details.*
import javax.inject.Inject


class PhotoDetailsFragment : Fragment() {

    val args: PhotoDetailsFragmentArgs by navArgs()

    private lateinit var appBarConfiguration: AppBarConfiguration

    val searchViewModel by activityViewModel { injector.SearchViewModel }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        photo_toolbar.setupWithNavController(navController, appBarConfiguration)

        val photo = searchViewModel.searchResultsLiveData.value?.firstOrNull {
            it.id == args.photoId
        }

        if (photo == null) {
            Snackbar.make(view, getString(R.string.missing_message), Snackbar.LENGTH_LONG).show()
            navController.navigateUp()
        } else {
            photo_title.text = photo.title
            photo_summary.text = photo.description
            Glide.with(view).load(photo.largeUrl).into(photo_image_view)
        }


        //Bug using library Some images causes class not found expection
     /*   searchViewModel.getPhoto(args.photoId).observe(this) {
            when (it) {
                is MySearchPhotoResponse.Success -> {
                    val photo = it.photos.first()
                    photo_title.text = photo.title
                    photo_summary.text = photo.description
                    Glide.with(view).load(photo.largeUrl).into(photo_image_view)
                }
                is MySearchPhotoResponse.Error -> {
                    Snackbar.make(view, it.message, Snackbar.LENGTH_LONG).show()
                    navController.navigateUp()
                }
            }
        }*/

        searchViewModel.getPhotoInfo(args.photoId).observe(this) {
            when (it) {
                is MyInfoPhotoResponse.Success -> {
                    photo_item_comment_text.text = it.photosMeta.comments.toString()
                    photo_item_fav_text.text = it.photosMeta.favorites.toString()
                }
                is MyInfoPhotoResponse.Error -> {

                }
            }
        }
    }


}
