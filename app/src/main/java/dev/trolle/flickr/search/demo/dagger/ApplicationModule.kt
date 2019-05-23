package dev.trolle.flickr.search.demo.dagger

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import dagger.Module
import dagger.Provides
import dev.trolle.flickr.search.demo.view.application.MyApplication

@Module
class ApplicationModule(
    private val myApplication: MyApplication
) {

    @Provides
    fun provideContext(): Context = myApplication.applicationContext


    @Provides
    fun provideFlickrClient(): Flickr {
        val apiKey = "1ea5662c832114783d2b336bbc0f7f1b"
        val sharedSecret = "347159d55ec25d9e"
        return Flickr(apiKey, sharedSecret, REST())
    }


    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)


}