package com.wein_business.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import com.wein_business.R
import com.wein_business.data.model.request.AddTripRequest
import com.wein_business.data.model.request.TripPriceRequest
import com.wein_business.data.providers.trip.AddTripProvider
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Constants
import com.wein_business.utils.Dialogs
import com.wein_business.utils.SessionManager
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.activity_add_trip.*
import kotlinx.android.synthetic.main.banner_header.*
import kotlin.properties.Delegates

class AddTripActivity : GenericActivity(),
    AddTripProvider.Listener {

    private lateinit var addTripProvider: AddTripProvider
    private var productId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        addTripProvider = AddTripProvider(this)

        if (intent.extras != null && intent.extras!!.getInt(Constants.KEY_PRODUCT_ID) != null) {
            productId = intent.extras!!.getInt(Constants.KEY_PRODUCT_ID)
            bindUI()
//            initListeners()
            addBackPressed()
        } else {
            finish()
        }
    }

    private fun bindUI() {
        tvTitle_header.text = Utility.getString(R.string.add_trip_title)

        tfSeats_add_trip.editText!!.addTextChangedListener {
            var providerPrice = (tfPrice_add_trip.editText!!.text.toString()).toInt()
            var numberOfSeats = (tfSeats_add_trip.editText!!.text.toString()).toInt()
            tvHint_add_trip.text = "مميز:   السعر  " + "${providerPrice + 100}" + "     عدد المقاعد  " + "${numberOfSeats + 10}" +
                    "\n"+"مميز جدا:   السعر  " + "${providerPrice + 500}" + "     عدد المقاعد  " + "${numberOfSeats + 20}"
        }
    }


//    private fun initListeners() {
//        tfStartTime_add_trip.editText!!.doAfterTextChanged{
//            tfEndTime_add_trip.editText!!.performClick()
//        }
//
//    }

    //**************************************************************
    //******Listeners***********************************************

    fun onClick(view: View) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
            btnSend_add_trip -> addTrip()
            tfStartDate_add_trip.editText -> {
                Dialogs.showGregorianDatePickerRange(
                    this,
                    tfStartDate_add_trip.editText!!,
                    tfEndDate_add_trip.editText!!,
                    todayIsMin = true,
                    todayIsMaximum = false,
                    isStartDate = true
                )
            }
            tfEndDate_add_trip.editText -> {
                Dialogs.showGregorianDatePickerRange(
                    this,
                    tfEndDate_add_trip.editText!!,
                    tfStartDate_add_trip.editText!!,
                    todayIsMin = true,
                    todayIsMaximum = false,
                    isStartDate = false
                )
            }
            tfStartTime_add_trip.editText -> {
                Dialogs.showTimePicker(this, tfStartTime_add_trip.editText!!)
            }
            tfEndTime_add_trip.editText -> {
                Dialogs.showTimePicker(this, tfEndTime_add_trip.editText!!)
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

    //***********************************************************************************
    //***********************************************************************************

    private fun addTrip() {
        //TODO Validation of fields becuase if empty crashhhhhhhhhh!!!!!

        var startDate = tfStartDate_add_trip.editText!!.text.toString()
        var endDate = tfEndDate_add_trip.editText!!.text.toString()
        var startTime = tfStartTime_add_trip.editText!!.text.toString()
        var endTime = tfEndTime_add_trip.editText!!.text.toString()

        //TODO REMOVE STANDARD ID AND REPLACE WITH DROPDOWN
        var providerPrice = (tfPrice_add_trip.editText!!.text.toString()).toInt()
        var numberOfSeats = (tfSeats_add_trip.editText!!.text.toString()).toInt()
        var prices: ArrayList<TripPriceRequest> = arrayListOf()
        prices.add(TripPriceRequest(Constants.TRIP_PRICE_CLASS_STANDARD, providerPrice, numberOfSeats))
        //TODO REMOVE
        prices.add(TripPriceRequest(2, providerPrice + 100, numberOfSeats + 10))
        prices.add(TripPriceRequest(3, providerPrice + 500, numberOfSeats + 20))

        showProgress()

        addTripProvider.addTrip(
            AddTripRequest(productId, startDate, endDate, startTime, endTime, prices)
        )
    }

    //***********************************************************************************
    //***********************************************************************************

    override fun onAddTripSuccess(message: String) {
        hideProgress()
        showSuccessToast(message)

        SessionManager.isNewTripCreated = true
    }

    override fun onAddTripFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

}