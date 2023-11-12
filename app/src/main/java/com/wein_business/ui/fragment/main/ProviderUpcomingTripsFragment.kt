package com.wein_business.ui.fragment.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.wein_business.R
import com.wein_business.data.model.trip.ProviderTripModel
import com.wein_business.data.providers.trip.ProviderUpcomingTripsProvider
import com.wein_business.ui.activity.MainActivity
import com.wein_business.ui.adapters.ProviderTripsAdapter
import com.wein_business.ui.fragment.generic.DataFragment
import com.wein_business.utils.Constants
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.fragment_provider_upcoming_trips.*

class ProviderUpcomingTripsFragment : DataFragment(),
    SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener,
    ProviderUpcomingTripsProvider.Listener {

    private lateinit var activity: MainActivity

    private lateinit var providerUpcomingTripsProvider: ProviderUpcomingTripsProvider
    private lateinit var skeletonProviderTrips: RecyclerViewSkeletonScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        providerUpcomingTripsProvider = ProviderUpcomingTripsProvider(this);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
        activity.providerUpcomingTripsFragment = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_provider_upcoming_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI(view)
        listeners()
        loadData()
    }

    private fun bindUI(view: View) {
        super.bind(view)

        swipeRefresh_provider_trips.setOnRefreshListener(this)
        Utility.setSwipeToRefreshColor(swipeRefresh_provider_trips, requireContext())

        RV_provider_upcoming_trips.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
    }

    private fun listeners() {
        btnOpenAttendance_provider_upcoming_trips.setOnClickListener(this)
    }

    //**************************************************************
    //******Listeners***********************************************
    override fun onClick(view: View) {
        when (view) {
            btnOpenAttendance_provider_upcoming_trips -> activity.activityTicketSearch()
        }
    }

    override fun onRefresh() {
        loadData()
    }

    //**************************************************************
    //**************************************************************

    override fun loadData() {
        if (isConnected())
            loadProviderTrips()
        else
            swipeRefresh_provider_trips.isRefreshing = false;
    }


    //**********************************************************
    //**********************************************************
    private fun loadProviderTrips() {
        skeletonProviderTrips = Skeleton.bind(RV_provider_upcoming_trips)
            .adapter(ProviderTripsAdapter(activity, ArrayList()))
            .load(R.layout.skeleton_item_product)
            .shimmer(true)
            .count(8)
            .frozen(true)
            .color(R.color.white)
            .duration(800)
            .show();

        providerUpcomingTripsProvider.getProviderTrips()
    }

    //**********************************************************
    //**********************************************************

    override fun onProviderUpcomingTripsSuccess(tripsList: ArrayList<ProviderTripModel>) {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefresh_provider_trips.isRefreshing = false;
            skeletonProviderTrips.hide();

            if (tripsList.isNotEmpty()) {
                RV_provider_upcoming_trips.adapter = ProviderTripsAdapter(activity, tripsList);
            } else {
                noData()
            }
        }, Constants.SKELETON_TIME);
    }

    override fun onProviderUpcomingTripsFail(errorMessage: String?) {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefresh_provider_trips.isRefreshing = false;
            skeletonProviderTrips.hide();
            errorData(errorMessage)
        }, Constants.SKELETON_TIME);
    }

}