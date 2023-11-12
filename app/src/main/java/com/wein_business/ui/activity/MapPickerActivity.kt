package com.wein_business.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.wein_business.R
import com.wein_business.services.GPSTracker
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Constants
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.activity_map_picker.*

class MapPickerActivity : GenericActivity(), OnCameraIdleListener, OnCameraMoveListener {
    private var googleMaps: GoogleMap? = null
    private var selectedCoordinates: LatLng? = null
    private var gpsTracker: GPSTracker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_picker)

        bindUI()
        addBackPressed()

        if (intent.extras != null) {
            val selectedCoordinatesText =
                intent.extras!!.getString(Constants.KEY_SELECTED_COORDINATES)
            if (selectedCoordinatesText != null && selectedCoordinatesText.replace(" ", "")
                    .isNotEmpty()
            ) {
                val coordinates =
                    selectedCoordinatesText.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                selectedCoordinates = LatLng(coordinates[0].toDouble(), coordinates[1].toDouble())
            }
        }
        initMaps()
        gpsTracker = GPSTracker(this)
    }

    private fun bindUI() {

    }

    //**************************************************************
    //******Listeners***********************************************

    fun onClick(v: View) {
        when (v.id) {
            R.id.btnCurrentLocation_map -> moveToCurrentLocation()
            R.id.btnMapView_map -> {
                if (googleMaps != null)
                    googleMaps!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            R.id.btnSatelliteView_map -> {
                if (googleMaps != null)
                    googleMaps!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            R.id.btn_select_location -> if (selectedCoordinates != null) {
                val selectedCoordinatesString =
                    selectedCoordinates!!.latitude.toString() + "," + selectedCoordinates!!.longitude
                val intent = Intent()
                intent.putExtra(Constants.KEY_SELECTED_COORDINATES, selectedCoordinatesString)
                setResult(RESULT_OK, intent)
                onBackPressedDispatcher.onBackPressed()
            } else {
                showErrorToast(Utility.getString(R.string.error_map_select_location))
            }
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

    private fun initMaps() {
        try {
            if (googleMaps == null) {
                //TODO deprecation
                val fragment = fragmentManager.findFragmentById(R.id.map_picker) as MapFragment
                fragment.getMapAsync { googleMap ->
                    googleMaps = googleMap
                    if (googleMaps != null) {
                        googleMaps!!.uiSettings.isZoomControlsEnabled = true
                        googleMaps!!.setOnCameraIdleListener(this@MapPickerActivity)
                        googleMaps!!.setOnCameraMoveListener(this@MapPickerActivity)
                        if (selectedCoordinates != null) googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                selectedCoordinates,
                                16f
                            )
                        ) else  //TODO DETECT CURRENT LOCATION WITH PERMISSIONS
                        //moveToCurrentLocation();
                            showDefaultLocation()
                    }

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun showDefaultLocation() {
        val currentLocation = LatLng(24.722058, 46.644569)
        googleMaps!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5f))
    }

    private fun moveToCurrentLocation() {
//        new TedPermission(this)
//                .setDeniedMessage(getResources().getString(R.string.location_deny_permisssion))
//                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//                .setPermissionListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//
//                        Location location = gpsTracker.getCurrentLocation();
//
//                        if (location != null) {
//                            selectedCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
//
//                            if ((selectedCoordinates.latitude != 0.0 && selectedCoordinates.longitude != 0.0)) {
//                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedCoordinates, 16));
//                            } else
//                                showDefaultLocation();
//                        } else
//                            showDefaultLocation();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(ArrayList<String> arrayList) {
//                    }
//                })
//                .check();
    }

    override fun onCameraIdle() {

        tv_map_pin_helper.visibility = View.GONE
        selectedCoordinates = googleMaps!!.cameraPosition.target
    }

    override fun onCameraMove() {
        tv_map_pin_helper.visibility = View.VISIBLE
    }
}