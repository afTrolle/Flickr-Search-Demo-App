package dev.trolle.flickr.search.demo.dagger

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Component
import dev.trolle.flickr.search.demo.view.activity.MainActivity
import dev.trolle.flickr.search.demo.view.application.MyApplication
import dev.trolle.flickr.search.demo.view.fragment.PhotoDetailsFragment
import dev.trolle.flickr.search.demo.view.fragment.PreferenceFragment
import dev.trolle.flickr.search.demo.view.fragment.example.ExampleViewModel
import dev.trolle.flickr.search.demo.viewModel.fragment.SearchViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(myApplication: MyApplication)

    fun inject(myApplication: MainActivity)
    fun inject(preferenceFragment: PreferenceFragment)
    fun inject(photoDetailsFragment: PhotoDetailsFragment)

    val exampleViewModel: ExampleViewModel

    val SearchViewModel: SearchViewModel
}


/*
* Provider return a view model (T : ViewModel)
* Wraps factory ViewModelProviders and  add create function
*
* Will call lambda if ViewModel does not exist
**/
inline fun <reified T : ViewModel> Fragment.viewModel(crossinline provider: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            val clazz = T::class.java
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass == clazz) {
                    @Suppress("UNCHECKED_CAST")
                    return provider() as T
                }
                throw IllegalArgumentException("Unexpected argument: $modelClass")
            }
        }
    }

/*
*  Activity scope view models provider
**/
inline fun <reified T : ViewModel> Fragment.activityViewModel(crossinline provider: () -> T) =
    activityViewModels<T> {
        object : ViewModelProvider.Factory {
            val clazz = T::class.java
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass == clazz) {
                    @Suppress("UNCHECKED_CAST")
                    return provider() as T
                }
                throw IllegalArgumentException("Unexpected argument: $modelClass")
            }
        }
    }