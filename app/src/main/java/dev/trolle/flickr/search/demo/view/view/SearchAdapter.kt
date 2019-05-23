package dev.trolle.flickr.search.demo.view.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.repository.flickr.MyPhoto
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dev.trolle.flickr.search.demo.extensions.dpToPx


class SearchAdapter(private val myDataset: List<MyPhoto>, private val onItemclick: (photoId: String) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    class ViewHolder(
        val myView: View,
        val image: ImageView,
        val title: TextView,
        val subTitle: TextView,
        val commentText: TextView,
        val favText: TextView,
        var currentPosition: Int
    ) : RecyclerView.ViewHolder(myView)

    //inflate my layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_item_linear, parent, false)

        val holder = ViewHolder(
            view,
            view.findViewById<View>(R.id.search_item_Image_view) as ImageView,

            view.findViewById<View>(R.id.search_item_image_title) as TextView,
            view.findViewById<View>(R.id.search_item_image_sub_title) as TextView,

            view.findViewById<View>(R.id.search_item_comment_text) as TextView,
            view.findViewById<View>(R.id.search_item_fav_text) as TextView,
            -1
        )

        //Item clicked with Id
        view.findViewById<View>(R.id.search_item_card_view).setOnClickListener {
            val position = holder.currentPosition
            val data = myDataset[position]
            onItemclick(data.id)
        }

        return holder
    }


    //update existing view with new content
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currentPosition = position

        //fix that if position is first element don't let the search bar block item
        val params = holder.myView.layoutParams as MarginLayoutParams
        params.topMargin = when (position) {
            0 -> 64
            else -> 0
        }.dpToPx

        params.bottomMargin = when (position) {
            myDataset.lastIndex -> 120
            else -> 0
        }.dpToPx

        val data = myDataset[position]
        val myView = holder.myView

        //TODO bind data set
        holder.title.text = data.title
        holder.subTitle.text = data.toUser.username

        holder.commentText.text = data.stats.comments.toString()
        holder.favText.text = data.stats.favorites.toString()

        Glide.with(holder.myView.context.applicationContext).load(data.url).into(holder.image)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = myDataset.size

}