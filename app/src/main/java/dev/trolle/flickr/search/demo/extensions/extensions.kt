package dev.trolle.flickr.search.demo.extensions

import android.content.res.Resources
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Observe live data as a lambda helper function
 */
fun <T> LiveData<T>.observe(lifecycle: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(lifecycle, Observer<T> {
        observer(it)
    })
}

/**
 * int represents dp convert it to Pixels
 */
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


/**
 * int represents pixels convert it to dp
 */
val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()