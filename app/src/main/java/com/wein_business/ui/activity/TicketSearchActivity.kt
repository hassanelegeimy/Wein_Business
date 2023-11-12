package com.wein_business.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wein_business.R
import com.wein_business.data.model.ticket.TicketSearchModel
import com.wein_business.data.providers.ticket.TicketSearchProvider
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.fragment.TicketDetailsSheetFragment
import com.wein_business.utils.Constants
import com.wein_business.utils.Dialogs
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.activity_ticket_search.*
import kotlinx.android.synthetic.main.banner_header.*

class TicketSearchActivity : GenericActivity(),
    TicketSearchProvider.Listener {

    private lateinit var ticketSearchProvider: TicketSearchProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_search)

        ticketSearchProvider = TicketSearchProvider(this)

        bindUI()
        addBackPressed()
    }

    private fun bindUI() {
        tvTitle_header.text = Utility.getString(R.string.attendance_title)
    }

    //**************************************************************
    //******Listeners***********************************************

    fun onClick(view: View) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
            btnScan_ticket_search -> startQrScanner()
            btnSearch_ticket_search -> validateQrCode()
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

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                try {
                    if (result.data != null && result.data!!.getStringExtra(Constants.KEY_SCANNED_QR) != null) {
                        var qrText = result.data!!.getStringExtra(Constants.KEY_SCANNED_QR)!!
                        getTicketDetails(qrText)
                    } else {
                        Dialogs.showErrorDialog(this, getString(R.string.no_data))
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private var qrScannerActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            var bookingCode = result.data!!.getStringExtra(Constants.QR_SCANNER_RESULT)
            if (bookingCode != null)
                getTicketDetails(bookingCode)
        } else {
            //TODO ERROR MESSAGE
        }
    }

    private fun startQrScanner() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            activityQrScanner(qrScannerActivityResultLauncher)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), Constants.CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

//***********************************************************************************
//***********************************************************************************

    private fun validateQrCode() {
        var bookingCode = tfQrCode_ticket_search.editText!!.text.toString()
        if (!bookingCode.isNullOrEmpty()) {
            getTicketDetails(bookingCode)
        } else {
            Dialogs.showErrorDialog(this, getString(R.string.validation_qr_code))
        }
    }

    private fun getTicketDetails(bookingCode: String) {
        showProgress()
        ticketSearchProvider.searchTicket(bookingCode)
    }

//***********************************************************************************
//***********************************************************************************

    override fun onTicketDetailsSuccess(bookingCode: String, ticketSearchModel: TicketSearchModel) {
        hideProgress()
        TicketDetailsSheetFragment().show(this, bookingCode, ticketSearchModel)
    }

    override fun onTicketDetailsFail(errorMessage: String) {
        hideProgress()
        showApiErrorToast(errorMessage)
    }

}