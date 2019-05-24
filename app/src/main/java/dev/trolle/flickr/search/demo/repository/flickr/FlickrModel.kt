package dev.trolle.flickr.search.demo.repository.flickr

import android.os.SystemClock
import com.flickr4java.flickr.people.User
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.comments.Comment
import com.flickr4java.flickr.stats.Stats


/* data classes */

data class MyPhoto(
    val id: String,
    val toUser: MyUser?,
    val datePosted: Long?,
    val dateTaken: Long?,
    val license: String?,
    val title: String?,
    val description: String?,
    val stats: MyStats?,
    val largeUrl: String?,
    val thumbnailUrl: String?,
    val largeSquare: String?,
    val smallSquare: String?,
    val url: String?,
    val mediumUrl: String?
) {

    companion object {
        val url = "https://alexander.af.trolle.dev/src/images/alex.jpg"
        fun mock() = MyPhoto(
            "1524",
            MyUser.mock(),
            SystemClock.currentThreadTimeMillis(),
            SystemClock.currentThreadTimeMillis(),
            "CC",
            "CV Image",
            "Image of me",
            MyStats.mock(),
            url,
            url,
            url,
            url,
            url,
            url
        )
    }

}

data class MyStats(val comments: Int, val favorites: Int, val views: Int) {
    companion object {
        fun mock() = MyStats(
            10,
            1337,
            2
        )
    }
}


data class MyUser(
    val id: String,
    val username: String,
    val realName: String?,
    val photosCount: Int,
    val description: String,
    val photosFirstDate: Long?
) {
    companion object {
        fun mock() = MyUser(
            "1524",
            "eric",
            "Alexander af Trolle",
            1,
            "nice guy",
            SystemClock.currentThreadTimeMillis()
        )
    }
}


/* Source Response classes */


sealed class MySearchPhotoResponse {
    data class Success(val photos: List<MyPhoto>) : MySearchPhotoResponse()
    data class Error(val message: String, val errorCode: String) : MySearchPhotoResponse()
}


sealed class MyInfoPhotoResponse {
    data class Success(
        val photos: MutableList<Comment>,
        val photosMeta: MyStats
    ) : MyInfoPhotoResponse()

    data class Error(val message: String, val errorCode: String) : MyInfoPhotoResponse()
}


/* convert data type functions  */


internal fun Photo.toMyPhoto(): MyPhoto =
    MyPhoto(
        this.id,
        this.owner?.toUser(),
        this.datePosted?.time,
        this.dateTaken?.time,
        this.license,

        this.title,
        this.description,
        this.stats?.toMyStats(),

        this.largeUrl,
        this.thumbnailUrl,

        this.squareLargeUrl,
        this.smallSquareUrl,

        this.url,
        this.mediumUrl
    )

internal fun Stats.toMyStats(): MyStats =
    MyStats(
        this.comments,
        this.favorites,
        this.views
    )


private fun User.toUser(): MyUser =
    MyUser(
        this.id,
        this.username,
        this.realName ?: "",
        this.photosCount,
        this.description ?: "",
        this.photosFirstDate?.time
    )

