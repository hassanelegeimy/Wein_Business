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
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.fragment.main.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : GenericActivity() {

    private val providerProductsFragment = ProviderProductsFragment()
    private val providerUpcomingTripsFragment = ProviderUpcomingTripsFragment()
    private val accountProviderFragment = AccountProviderFragment()

    var active: Fragment = providerProductsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindUI()

        //TODO
        askNotificationPermission()
    }

    private fun bindUI() {
        initNavigationMenu()
    }


    private fun initNavigationMenu() {
       supportFragmentManager.beginTransaction().add(R.id.layout_main_container, accountProviderFragment, "3").hide(accountProviderFragment).commit();
       supportFragmentManager.beginTransaction().add(R.id.layout_main_container, providerUpcomingTripsFragment, "2").hide(providerUpcomingTripsFragment).commit();
       supportFragmentManager.beginTransaction().add(R.id.layout_main_container, providerProductsFragment, "1").commit();

        initListeners()
    }

    private fun initListeners() {
        bottomNavigation_main.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_products -> navAction(providerProductsFragment)
                R.id.nav_trips -> navAction(providerUpcomingTripsFragment)
                R.id.nav_account -> navAction(accountProviderFragment)
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun navAction(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    //***********************************************************************************
    //***********************************************************************************

    override fun onBackPressed() {
        if (active is ProviderProductsFragment)
            providerProductsFragment.onBackPressed()
        else
            bottomNavigation_main.findViewById<View>(R.id.nav_products).performClick()
    }

    //***********************************************************************************
    //** Notifications ******************************************************************

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getFirebaseToken()
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this, "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG
            ).show()
        }
    }

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
            }
        }
    }

    private fun getFirebaseToken() {
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