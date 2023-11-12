package com.wein_business.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wein_business.R
import com.wein_business.data.model.ticket.TicketSearchModel
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.adapters.TicketSearchPriceClassesAdapter
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.fragment_sheet_ticket_details.*


class TicketDetailsSheetFragment : BottomSheetDialogFragment() {
    private lateinit var activity: GenericActivity
    private lateinit var bookingCode: String
    private lateinit var ticketSearchModel: TicketSearchModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    fun show(
        activity: GenericActivity,
        bookingCode:String ,
        ticketSearchModel: TicketSearchModel
    ) {
        show(activity.supportFragmentManager, TicketDetailsSheetFragment::class.java.simpleName)
        this.activity = activity
        this.bookingCode = bookingCode
        this.ticketSearchModel = ticketSearchModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sheet_ticket_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    fun bindUI() {

        if (ticketSearchModel.imageMaster.isNotEmpty()) {
            Glide.with(this)
                .load(Utility.getDownloadFileUrl(ticketSearchModel.imageMaster))
                .placeholder(R.color.loading_view)
                .error(R.color.loading_view)
                .centerCrop()
                .into(image_ticket_search)
        }

        tvTitle_ticket_search.text = ticketSearchModel.title
        tvStartDate_ticket_search.text = ticketSearchModel.startDate
        tvEndDate_ticket_search.text = ticketSearchModel.endDate
        tvStartTime_ticket_search.text = ticketSearchModel.startTime
        tvEndTime_ticket_search.text = ticketSearchModel.endTime

        tvQR_ticket_search.text = bookingCode
        Utility.showQRImage(bookingCode, imageQR_ticket_search)

        RvPrices_ticket_search.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        Utility.addRecyclerViewDivider(activity, RvPrices_ticket_search)

        if (ticketSearchModel.priceClasses.isNotEmpty())
            RvPrices_ticket_search.adapter = TicketSearchPriceClassesAdapter(activity, ticketSearchModel.priceClasses)
        else
            RvPrices_ticket_search.adapter = TicketSearchPriceClassesAdapter(activity, arrayListOf())
    }
}