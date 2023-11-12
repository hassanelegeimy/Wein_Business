package com.wein_business.ui.adapters.pager

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.item_images_viewer.view.*

class ImageViewerPagerAdapter(
    private val activity: GenericActivity,
    private val itemsList: ArrayList<String>,
    private val isFullScreenViewer: Boolean
) : RecyclerView.Adapter<ImageViewerPagerAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_images_viewer, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemsList[position].isNotEmpty()) {

            if (isFullScreenViewer) { //images viewer pager
                Glide.with(activity)
                    .load(Utility.getDownloadFileUrl(itemsList[position]))
                    .placeholder(R.drawable.ic_no_image)
                    .fitCenter()
                    .into(holder.photoview_itemImageViewer)
            } else { //details images pager
                Glide.with(activity)
                    .load(Utility.getDownloadFileUrl(itemsList[position]))
                    .placeholder(R.drawable.ic_no_image)
                    .centerCrop()
                    .into(holder.photoview_itemImageViewer)
            }
        }

        if (!isFullScreenViewer) { //product details images pager
            holder.itemView.setOnClickListener {
                activity.activityImagesViewer(itemsList , position);
            }

            //disable zoom in details images pager
            holder.photoview_itemImageViewer.setOnTouchListener(View.OnTouchListener { v: View?, event: MotionEvent? -> false })
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return itemsList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoview_itemImageViewer = view.photoview_itemImageViewer
    }
}