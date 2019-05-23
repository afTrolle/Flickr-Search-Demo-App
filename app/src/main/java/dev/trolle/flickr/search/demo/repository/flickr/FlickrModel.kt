package dev.trolle.flickr.search.demo.repository.flickr

import android.os.SystemClock
import androidx.room.*


data class MyPhoto(
    @PrimaryKey val id: String,
    val toUser: MyUser,
    val datePosted: Long,
    val dateTaken: Long,
    val license: String,
    val title: String,
    val description: String,
    val stats: MyStats,
    val thumbnailUrl: String,
    val squareLargeUrl: String,
    val smallSquareUrl: String,
    val url4: String,
    val url: String
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
            url
        )
    }


}

data class MyStats(val comments: Int, val favorites: Int, val views: Int) {
    companion object {
        fun mock() = MyStats(10, 1337, 2)
    }
}

data class MyUser(
    @PrimaryKey val id: String,
    val username: String,
    val realName: String,
    val photosCount: Int,
    val description: String,
    val photosFirstDate: Long
) {
    companion object {
        fun mock() = MyUser("1524", "eric", "Alexander af Trolle", 1, "nice guy", SystemClock.currentThreadTimeMillis())
    }
}


//@Dao
interface PhotoDao {
    @Insert
    fun insert(photo: MyPhoto)

    @Update
    fun update(photo: MyPhoto)

    @Delete
    fun delete(photo: MyPhoto)

//    @Query("Select * FROM photo WHERE toUser. LIKE :userName")
//    fun findPhotoByUserName(userName: String): List<MyPhoto>

    @RawQuery
    fun getThroughRaw(query: RoomSQLiteQuery): MyPhoto
}


//@Database(entities = [MyPhoto::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

}