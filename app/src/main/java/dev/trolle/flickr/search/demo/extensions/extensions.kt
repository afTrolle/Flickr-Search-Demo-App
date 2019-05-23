package dev.trolle.flickr.search.demo.extensions

import android.content.res.Resources
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun <T> LiveData<T>.observe(lifecycle: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(lifecycle, Observer<T> {
        observer(it)
    })
}

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()