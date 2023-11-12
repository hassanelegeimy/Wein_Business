package com.wein_business.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wein_business.R
import com.wein_business.data.model.trip.ProviderTripPriceModel
import com.wein_business.ui.activity.generic.GenericActivity
import kotlinx.android.synthetic.main.item_provider_trip_price.view.*

class ProviderTripPricesAdapter(
    private var activity: GenericActivity,
    private var itemsList: ArrayList<ProviderTripPriceModel>
) :
    RecyclerView.Adapter<ProviderTripPricesAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.item_provider_trip_price, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val providerTripPriceModel = itemsList[position]

        holder.tvClass_item_trip_price.text = providerTripPriceModel.priceClass.toString()
        holder.tvPrice_item_trip_price.text = "${providerTripPriceModel.price}"
        holder.tvTotalSeats_item_trip_price.text = "${providerTripPriceModel.numberOfSeats}"
        holder.tvBookedSeats_item_trip_price.text =
            "${providerTripPriceModel.numberOfReservedSeats}"

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvClass_item_trip_price: TextView =
            itemView.tvClass_item_trip_price
        var tvPrice_item_trip_price: TextView =
            itemView.tvPrice_item_trip_price
        var tvTotalSeats_item_trip_price: TextView =
            itemView.tvTotalSeats_item_trip_price
        var tvBookedSeats_item_trip_price: TextView =
            itemView.tvBookedSeats_item_trip_price
    }
}