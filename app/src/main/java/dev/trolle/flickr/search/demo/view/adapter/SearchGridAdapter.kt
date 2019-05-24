package dev.trolle.flickr.search.demo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.extensions.dpToPx
import dev.trolle.flickr.search.demo.repository.flickr.MyPhoto

class SearchGridAdapter(private val myDataSet: List<MyPhoto>, private val onItemClick: (photoId: String) -> Unit) :
    RecyclerView.Adapter<SearchGridAdapter.ViewHolder>() {

    class ViewHolder(
        val myView: View,
        val image: ImageView,
        val title: TextView,
        val commentText: TextView,
        val favText: TextView,
        var currentPosition: Int
    ) : RecyclerView.ViewHolder(myView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_item_grid, parent, false)

        val holder = ViewHolder(
            view,
            view.findViewById<View>(R.id.search_item_Image_view) as ImageView,

            view.findViewById<View>(R.id.search_item_image_title) as TextView,

            view.findViewById<View>(R.id.search_item_comment_text) as TextView,
            view.findViewById<View>(R.id.search_item_fav_text) as TextView,
            -1
        )

        view.findViewById<View>(R.id.search_item_card_view).setOnClickListener {
            val position = holder.currentPosition
            val data = myDataSet[position]
            onItemClick(data.id)
        }

        return holder
    }

    override fun getItemCount(): Int = myDataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = myDataSet[position]
        //Update mutable state of view holder
        holder.currentPosition = position

        //fix that if position is first element don't let the search bar block item
        val params = holder.myView.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = when (position) {
            0 -> 64
            1 -> 64
            else -> 0
        }.dpToPx

        params.bottomMargin = when (position) {
            myDataSet.lastIndex -> 120
            myDataSet.lastIndex - 1 -> 120
            else -> 0
        }.dpToPx

        holder.title.text = data.title
        holder.commentText.text = data.stats?.comments.toString()
        holder.favText.text = data.stats?.favorites.toString()

        Glide.with(holder.myView.context.applicationContext).load(data.largeSquare).into(holder.image)
    }


}