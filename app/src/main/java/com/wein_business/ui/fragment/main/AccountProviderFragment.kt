package com.wein_business.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wein_business.R
import com.wein_business.data.model.ProviderStatusModel
import com.wein_business.data.providers.profile.ProviderStatusProvider
import com.wein_business.ui.activity.MainActivity
import com.wein_business.ui.fragment.generic.GenericFragment
import com.wein_business.utils.NetworkUtils
import com.wein_business.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_account_provider.*

class AccountProviderFragment : GenericFragment(),
    ProviderStatusProvider.Listener,
    SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

    private lateinit var activity: MainActivity

    private lateinit var providerStatusProvider: ProviderStatusProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity

        providerStatusProvider = ProviderStatusProvider(this)
    }

    override fun onResume() {
        super.onResume()
        bindUser()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && isResumed) {
            onResume()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_account_provider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUser()
        initListeners()
        loadData()
    }

    private fun bindUser() {
        tvWelcomeProvider_account.text =
            getString(R.string.welcome) + " " + SessionManager.userModel.displayName

        tvStatusProvider_account.text = SessionManager.providerStatusModel.userStatusLookup.toString()
    }

    private fun initListeners() {
        swipeRefreshProvider_account.setOnRefreshListener(this)

        btnProfileProvider_account.setOnClickListener(this)
        btnWalletProvider_account.setOnClickListener(this)
        btnLogoutProvider_account.setOnClickListener(this)

        btnNotifications_account.setOnClickListener(this)
        btnComplaints_account.setOnClickListener(this)
        btnContactUs_account.setOnClickListener(this)
        btnChangeLang_account.setOnClickListener(this)
        btnShareApp_account.setOnClickListener(this)
        btnRateApp_account.setOnClickListener(this)
    }

    //**************************************************************
    //**************************************************************

    override fun onClick(view: View) {
        when (view) {

            btnProfileProvider_account -> {
                if (SessionManager.isUserCompany) {
                    activity.activityCompanyProfile()
                } else if (SessionManager.isUserIndividual) {
                    activity.activityIndividualProfile()
                }
            }

            btnWalletProvider_account -> {}
            btnLogoutProvider_account -> SessionManager.logout(activity)

            btnNotifications_account -> {}
            btnComplaints_account -> {}
            btnContactUs_account -> {}
            btnChangeLang_account -> {}
            btnShareApp_account -> {}
            btnRateApp_account -> {}
        }
    }

    override fun onRefresh() {
        loadData()
    }

    //**************************************************************
    //**************************************************************

    fun loadData() {
        if (NetworkUtils.isConnected())
            getProviderStatus()
        else
            swipeRefreshProvider_account.isRefreshing = false;
    }

    private fun getProviderStatus() {
        swipeRefreshProvider_account.isRefreshing = true;
        providerStatusProvider.getProviderStatus()
    }

    //**************************************************************
    //**************************************************************

    override fun onProviderStatusSuccess(providerStatusModel: ProviderStatusModel) {
        swipeRefreshProvider_account.isRefreshing = false;

        SessionManager.changeProviderStatusModel(providerStatusModel)
        bindUser()
    }

    override fun onProviderStatusFail(message: String) {
        swipeRefreshProvider_account.isRefreshing = false;
        //Not showing the message
    }


//**********************************************************
//**********************************************************
//    fun createSharingLink() {
//        FirebaseDynamicLinks.getInstance().createDynamicLink()
//            .setLink(Uri.parse("http://apphost3.ddns.net:4444/shareKhair/?invitedBy=Hassan Abdu 123"))
//            .setDomainUriPrefix("https://newlineoffers.page.link/")
//            .setAndroidParameters(
//                Builder("com.newline.offers")
//                    .build()
//            )
//            .setIosParameters(
//                Builder("com.newline.offers")
//                    .setAppStoreId("310633997") //                                .setMinimumVersion("1.0.1")
//                    .build()
//            )
//            .buildShortDynamicLink()
//            .addOnSuccessListener(object : OnSuccessListener<ShortDynamicLink?>() {
//                fun onSuccess(shortDynamicLink: ShortDynamicLink) {
////                        Uri shortLink = task.getResult().getShortLink();
////                        Uri flowchartLink = task.getResult().getPreviewLink();
//                    val shortLink: Uri = shortDynamicLink.getShortLink()
//                    Log.e("hassan ", "short link $shortLink")
//                    // share app dialog
//                    val intent = Intent()
//                    intent.setAction(Intent.ACTION_SEND)
//                    intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString())
//                    intent.setType("text/plain")
//                    startActivity(intent)
//                }
//            })
//            .addOnFailureListener(object : OnFailureListener() {
//                fun onFailure(e: Exception) {
//                    Log.e("hassan ", "short link error" + e.message)
//                }
//            })
//    }
}