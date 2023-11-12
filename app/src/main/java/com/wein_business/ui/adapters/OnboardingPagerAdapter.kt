package com.wein_business.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wein_business.R
import kotlinx.android.synthetic.main.item_onboarding.view.*

class OnboardingPagerAdapter(
    private val onboarding_titles: List<String>,
    private val onboarding_descriptions: List<String>,
    private val imageList: List<Int>
) : RecyclerView.Adapter<OnboardingPagerAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_item_onboarding_title.text = onboarding_titles[position]
        holder.tv_item_onboarding_description.text = onboarding_descriptions[position]
        holder.image_item_onboarding.setImageResource(imageList[position])
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return onboarding_titles.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_item_onboarding_title: TextView = view.tvTitle_itemOnboarding
        val tv_item_onboarding_description: TextView = view.tvDescription_itemOnboarding
        val image_item_onboarding: ImageView = view.image_itemOnboarding
    }
}