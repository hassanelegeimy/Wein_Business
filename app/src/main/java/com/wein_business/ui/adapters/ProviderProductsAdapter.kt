package com.wein_business.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wein_business.R
import com.wein_business.data.model.product.ProviderProductModel
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.item_product_provider.view.*

class ProviderProductsAdapter(
    private var activity: GenericActivity,
    private var itemsList: ArrayList<ProviderProductModel>
) :
    RecyclerView.Adapter<ProviderProductsAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.item_product_provider, parent, false)



        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val providerProductModel = itemsList[position]

        holder.tvTitle_itemProductProvider.text = providerProductModel.title
        holder.tvCity_itemProductProvider.text = providerProductModel.city.toString()
        holder.tvStatus_itemProductProvider.text = providerProductModel.status.statusLookup.toString()
        //************************************************************************************
        if (!providerProductModel.imageMaster.isNullOrEmpty()) {
            Glide.with(activity)
                .load(Utility.getDownloadFileUrl(providerProductModel.imageMaster))
                .placeholder(R.color.loading_view)
                .error(R.color.loading_view)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(holder.image_itemProductProvider)
        } else {
            holder.image_itemProductProvider.setImageResource(R.color.loading_view)
        }
        //************************************************************************************
        //************************************************************************************
        holder.itemView.setOnClickListener {
            activity.activityCreateUpdateProduct(providerProductModel.id)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_itemProductProvider: ImageView = itemView.image_itemProductProvider
        var tvTitle_itemProductProvider: TextView = itemView.tvTitle_itemProductProvider
        var tvCity_itemProductProvider: TextView = itemView.tvCity_itemProductProvider
        var tvStatus_itemProductProvider: TextView = itemView.tvStatus_itemProductProvider
    }
}