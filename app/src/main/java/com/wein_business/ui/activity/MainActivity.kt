package com.wein_business.ui.activity

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.adapters.pager.MainPagerAdapter
import com.wein_business.ui.fragment.main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.banner_navigation.*

class MainActivity : GenericActivity() {

    private var navAdapter: MainPagerAdapter? = null
    var providerProductsFragment: ProviderProductsFragment? = null
    var providerUpcomingTripsFragment: ProviderUpcomingTripsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindUI()
        bindUser()

        //TODO
        askNotificationPermission()

    }

    private fun bindUI() {

    }

    private fun bindUser() {
        initPager()
    }

    private fun initPager() {
        navAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        pager_main.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager_main.adapter = navAdapter
        pager_main.currentItem = navAdapter!!.positionProducts
        pager_main.offscreenPageLimit = 3
        pager_main.isUserInputEnabled = false

        pager_main.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }
        })
    }

    //***********************************************************************************
    //***********************************************************************************

    fun onClick(view: View) {
        when (view) {
            Rb_navigationProducts -> pager_main.currentItem = navAdapter!!.positionProducts
            Rb_navigationTrips -> pager_main.currentItem = navAdapter!!.positionTRIPS
            Rb_navigationAccount -> pager_main.currentItem = navAdapter!!.positionACCOUNT
        }
    }

    override fun onBackPressed() {
        when (pager_main.currentItem) {
            navAdapter!!.positionProducts -> providerProductsFragment!!.onBackPressed()//homeFragment?.onBackPressed()
            else -> navHome()
        }
    }

    private fun navHome() {
        Rb_navigationProducts.performClick()
    }

    //***********************************************************************************
    //***********************************************************************************

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getFirebaseToken()
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG).show()
        }}

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {  // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } }}

    private fun getFirebaseToken(){
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get FCM registration token
            val token = task.result
            Log.d(TAG, "Firebase getTokenMain: $token")
        })
    }



}