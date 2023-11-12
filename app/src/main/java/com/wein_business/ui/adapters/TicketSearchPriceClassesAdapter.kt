package com.wein_business.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wein_business.R
import com.wein_business.data.model.ticket.TicketSearchPriceModel
import com.wein_business.ui.activity.generic.GenericActivity
import kotlinx.android.synthetic.main.item_ticket_search_priceclass.view.*

class TicketSearchPriceClassesAdapter(
    private var activity: GenericActivity,
    private var itemsList: ArrayList<TicketSearchPriceModel>
) :
    RecyclerView.Adapter<TicketSearchPriceClassesAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.item_ticket_search_priceclass, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticketPriceModel = itemsList[position]

        holder.tvClass_item_ticket_priceclass.text = ticketPriceModel.priceClasses.toString()
        holder.tvSeatsCount_item_ticket_priceclass.text ="${ticketPriceModel.seatsCount}"
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvClass_item_ticket_priceclass: TextView =
            itemView.tvClass_item_ticket_priceclass
        var tvSeatsCount_item_ticket_priceclass: TextView =
            itemView.tvSeatsCount_item_ticket_priceclass

    }
}