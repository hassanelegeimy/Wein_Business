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
import com.wein_business.data.model.product.ProviderProductModel
import com.wein_business.data.providers.product.ProviderProductsProvider
import com.wein_business.ui.activity.MainActivity
import com.wein_business.ui.adapters.ProviderProductsAdapter
import com.wein_business.ui.fragment.generic.DataFragment
import com.wein_business.utils.Constants
import com.wein_business.utils.SessionManager
import com.wein_business.utils.Utility
import com.wein_business.R
import com.wein_business.utils.Dialogs
import kotlinx.android.synthetic.main.fragment_provider_products.*
import kotlinx.android.synthetic.main.layout_data_error.*

class ProviderProductsFragment : DataFragment(),
    ProviderProductsProvider.Listener,
    SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {
    private lateinit var activity: MainActivity

    private lateinit var providerProductsProvider: ProviderProductsProvider
    private lateinit var skeletonProviderActivities: RecyclerViewSkeletonScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity

        providerProductsProvider = ProviderProductsProvider(this)
    }

    override fun onResume() {
        super.onResume()

        bindUser()

        if (SessionManager.isNewProductCreatedUpdated) {
            SessionManager.isNewProductCreatedUpdated = false
            loadData()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && isResumed) {
            onResume()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_provider_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI(view)
        bindUser()
        initListeners()
        loadData()
    }

    private fun bindUI(view: View) {
        super.bind(view) //bind Empty layout

        Utility.setSwipeToRefreshColor(swipeRefresh_provider_products, requireContext())

        RV_provider_products.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

    }

    private fun bindUser() {
        tvWelcome_home.text =
            getString(R.string.welcome) + " " + SessionManager.userModel.displayName
    }

    private fun initListeners() {
        swipeRefresh_provider_products.setOnRefreshListener(this)
        btnAddNew_provider_product.setOnClickListener(this)
    }

    //**************************************************************
    //******Listeners***********************************************

    override fun onClick(view: View) {
        when (view) {
            btnAddNew_provider_product -> {
                //TODO UNCOMMENT THISSSS
//                if (SessionManager.isProviderStatusActive()) {
                activity.activityCreateUpdateProduct(Constants.NEW_PRODUCT_DEFAULT_VALUE)
//                } else {
//                    Dialogs.showErrorToast(activity, getString(R.string.error_not_active_provider))
//                }
            }

        }
    }

    override fun onRefresh() {
        loadData()
    }

    private var doubleBackPressedOnce = false
    fun onBackPressed() {
        if (doubleBackPressedOnce) {
            Utility.exitApp(activity)
            return
        }
        doubleBackPressedOnce = true
        Dialogs.showToast(activity, Utility.getString(R.string.exit_message))
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressedOnce = false }, 2000)
    }

    //**************************************************************
    //**************************************************************

    override fun loadData() {
        if (isConnected())
            getProviderProducts()
        else
            swipeRefresh_provider_products.isRefreshing = false;
    }


    //**********************************************************
    //**********************************************************
    private fun getProviderProducts() {
        skeletonProviderActivities = Skeleton.bind(RV_provider_products)
            .adapter(ProviderProductsAdapter(activity, ArrayList()))
            .load(R.layout.skeleton_item_product)
            .shimmer(true)
            .count(5)
            .frozen(true)
            .color(R.color.white)
            .duration(800)
            .show();

        providerProductsProvider.getProviderProducts()
    }

    //**********************************************************
//**********************************************************
    override fun onProvidersProductsSuccess(providerProductsList: ArrayList<ProviderProductModel>) {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefresh_provider_products.isRefreshing = false;
            skeletonProviderActivities.hide();

            if (providerProductsList.isNotEmpty()) {
                RV_provider_products.adapter =
                    ProviderProductsAdapter(activity, providerProductsList);
                layout_data_error.visibility = View.GONE
            } else {
                layout_data_error.visibility = View.VISIBLE
                noData()
            }
        }, Constants.SKELETON_TIME);

    }

    override fun onProvidersProductsFail(errorMessage: String?) {
        Handler(Looper.getMainLooper()).postDelayed({
            swipeRefresh_provider_products.isRefreshing = false
            skeletonProviderActivities.hide();
            errorData(errorMessage)

        }, Constants.SKELETON_TIME)
    }

}