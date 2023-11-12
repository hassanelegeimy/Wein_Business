package com.wein_business.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wein_business.R
import com.wein_business.data.model.trip.ProviderTripModel
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.item_provider_trip.view.*

class ProviderTripsAdapter(
    private var activity: GenericActivity,
    private var itemsList: ArrayList<ProviderTripModel>
) :
    RecyclerView.Adapter<ProviderTripsAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.item_provider_trip, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val providerTripModel = itemsList[position]

        if (providerTripModel.imageMaster.isNotEmpty()) {
            Glide.with(activity)
                .load(Utility.getDownloadFileUrl(providerTripModel.imageMaster))
                .placeholder(R.color.loading_view)
                .error(R.color.loading_view)
                .centerCrop()
                .into(holder.image_item_product_provider_trip)
        }

        holder.tvTitle_item_product_provider_trip.text = providerTripModel.title
        holder.tvStartTime_item_product_provider_trip.text = providerTripModel.startTime
        holder.tvEndTime_item_product_provider_trip.text = providerTripModel.endTime
        holder.tvStartDate_item_product_provider_trip.text = providerTripModel.startDate
        holder.tvEndDate_item_product_provider_trip.text = providerTripModel.endDate

        holder.RvPrices_item_product_provider_trip.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        Utility.addRecyclerViewDivider(activity, holder.RvPrices_item_product_provider_trip)

        if (providerTripModel.prices.isNotEmpty()) {
            holder.RvPrices_item_product_provider_trip.adapter = ProviderTripPricesAdapter(activity, providerTripModel.prices);
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_item_product_provider_trip: ImageView =
            itemView.image_item_product_provider_trip
        var tvTitle_item_product_provider_trip: TextView =
            itemView.tvTitle_item_product_provider_trip
        var tvStartTime_item_product_provider_trip: TextView =
            itemView.tvStartTime_item_product_provider_trip
        var tvEndTime_item_product_provider_trip: TextView =
            itemView.tvEndTime_item_product_provider_trip
        var tvStartDate_item_product_provider_trip: TextView =
            itemView.tvStartDate_item_product_provider_trip
        var tvEndDate_item_product_provider_trip: TextView =
            itemView.tvEndDate_item_product_provider_trip
        var RvPrices_item_product_provider_trip: RecyclerView =
            itemView.RvPrices_item_product_provider_trip
    }
}