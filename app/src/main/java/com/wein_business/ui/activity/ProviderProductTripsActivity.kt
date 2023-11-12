package com.wein_business.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.wein_business.R
import com.wein_business.data.model.trip.ProviderTripModel
import com.wein_business.data.providers.trip.ProviderProductTripsProvider
import com.wein_business.ui.activity.generic.DataActivity
import com.wein_business.ui.adapters.ProviderTripsAdapter
import com.wein_business.utils.Constants
import com.wein_business.utils.SessionManager
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.activity_provider_product_trips.*
import kotlinx.android.synthetic.main.banner_header.*
import kotlin.properties.Delegates

class ProviderProductTripsActivity : DataActivity(),
    SwipeRefreshLayout.OnRefreshListener,
    ProviderProductTripsProvider.Listener {

    private lateinit var providerProductTripsProvider: ProviderProductTripsProvider
    private lateinit var skeleton: RecyclerViewSkeletonScreen
    private var productId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_product_trips)

        providerProductTripsProvider = ProviderProductTripsProvider(this)

        if (intent.extras != null && intent.extras!!.getInt(Constants.KEY_PRODUCT_ID) != null) {
            productId = intent.extras!!.getInt(Constants.KEY_PRODUCT_ID)
            bindUI()
            addBackPressed()
            loadData()
        } else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        if (SessionManager.isNewTripCreated) {
            SessionManager.isNewTripCreated = false
            loadData()
        }
    }

    private fun bindUI() {
        super.bindListPlaceholder()
        tvTitle_header.text = Utility.getString(R.string.provider_product_trips_title)

        swipeRefresh_provider_product_trips.setOnRefreshListener(this)
        Utility.setSwipeToRefreshColor(swipeRefresh_provider_product_trips, this)

        RV_provider_product_trips.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    //**************************************************************
    //******Listeners***********************************************

    fun onClick(view: View) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
            btnAddNew_provider_product_trip -> activityAddTrip(productId)

        }
    }

    private fun addBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                animateScreenTransition()
            }
        })
    }

    override fun onRefresh() {
        loadData()
    }
    //***********************************************************************************
    //***********************************************************************************

    override fun loadData() {
        if (isConnected())
            getProviderProductTrips()
        else
            swipeRefresh_provider_product_trips.isRefreshing = false;
    }

    //**********************************************************
    //**********************************************************

    private fun getProviderProductTrips() {
        skeleton = Skeleton.bind(RV_provider_product_trips)
            .adapter(ProviderTripsAdapter(this, ArrayList()))
            .load(R.layout.skeleton_item_product)//TODO CHANGE
            .shimmer(true)
            .count(8)
            .frozen(true)
            .color(R.color.white)
            .duration(800)
            .show();

        providerProductTripsProvider.getProviderProductTrips(productId)
    }

    //***********************************************************************************
    //***********************************************************************************

    override fun onProviderProductTripsSuccess(tripsList: ArrayList<ProviderTripModel>) {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefresh_provider_product_trips.isRefreshing = false;
            skeleton.hide();

            if (tripsList.isNotEmpty()) {
                RV_provider_product_trips.adapter = ProviderTripsAdapter(this, tripsList);
            } else {
                noData()
            }
        }, Constants.SKELETON_TIME);
    }

    override fun onProviderProductTripsFail(errorMessage: String?) {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefresh_provider_product_trips.isRefreshing = false;
            skeleton.hide();
            errorData(errorMessage)

        }, Constants.SKELETON_TIME);
    }

}