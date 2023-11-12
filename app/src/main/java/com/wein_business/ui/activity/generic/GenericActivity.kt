package com.wein_business.ui.activity.generic

import android.app.Dialog
import android.content.Intent
import android.os.*
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.wein_business.BaseApp
import com.wein_business.ui.activity.*
import com.wein_business.ui.activity.QrScannerActivity
import com.wein_business.ui.activity.profile.CompanyProfileActivity
import com.wein_business.ui.activity.profile.IndividualProfileActivity
import com.wein_business.utils.*
import com.wein_business.R

open class GenericActivity : AppCompatActivity() {

    lateinit var loaderDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loaderDialog = Dialogs.loaderDialog(this)

        LocaleHelper2.onAttach(this)
    }

    override fun onResume() {
        super.onResume()
        LocaleHelper2.onAttach(this)
        BaseApp.currentActivity = this
    }

    //------------------------------------------------------
    //---------------Start Activities-----------------------

    fun activityOnBoarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
        animateScreenTransition()
    }

    fun activityMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)

        startActivity(intent)
        finish()
        animateScreenTransition()
    }

    fun activityLogin(restartRequired: Boolean) {
        val intent = Intent(this, LoginActivity::class.java)
        if (restartRequired)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        else
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)

        startActivity(intent)
        finish()
        animateScreenTransition()
    }

    fun activityRegistration() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityCompanyProfile() {
        val intent = Intent(this, CompanyProfileActivity::class.java)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityIndividualProfile() {
        val intent = Intent(this, IndividualProfileActivity::class.java)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityCreateUpdateProduct(productId: Int) {
        val intent = Intent(this, CreateUpdateProductActivity::class.java)
        intent.putExtra(Constants.KEY_PRODUCT_ID, productId)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityProviderProductTrips(productId: Int) {
        val intent = Intent(this, ProviderProductTripsActivity::class.java)
        intent.putExtra(Constants.KEY_PRODUCT_ID, productId)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityAddTrip(productId: Int) {
        val intent = Intent(this, AddTripActivity::class.java)
        intent.putExtra(Constants.KEY_PRODUCT_ID, productId)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityTicketSearch() {
        val intent = Intent(this, TicketSearchActivity::class.java)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityMapsPicker(mapResult: ActivityResultLauncher<Intent>, currentLatLang: String) {
        val intent = Intent(this, MapPickerActivity::class.java)
        intent.putExtra(Constants.KEY_SELECTED_COORDINATES, currentLatLang)
        mapResult.launch(intent)
        animateScreenTransition()
    }

    fun activityImagesViewer(imagesList: ArrayList<String>, position: Int) {
        val intent = Intent(this, ImagesViewerActivity::class.java)
        intent.putExtra(Constants.KEY_IMAGES_LIST, imagesList)
        intent.putExtra(Constants.KEY_IMAGE_POSITION, position)
        startActivity(intent)
        animateScreenTransition()
    }

    fun activityVideoViewer(fileId: String) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra(Constants.KEY_VIDEO_URL, fileId)
        startActivity(intent)
        animateScreenTransition()
    }

    fun imageViewerSingleImage(fileId: String) {
        var imagesList = ArrayList<String>()
        imagesList.add(fileId)
        activityImagesViewer(imagesList, 0)
        animateScreenTransition()
    }

    fun activityQrScanner(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(this, QrScannerActivity::class.java)
        launcher.launch(intent)
        animateScreenTransition()
    }

    //************************************************************
    fun showApiErrorToast(message: String) {
        showErrorToast(message)
    }

    fun showSuccessToast(message: String) {
        Dialogs.showSuccessDialog(this, message)
    }

    fun showAlertToast(message: String) {
        Dialogs.showAlertDialog(this, message)
    }

    fun showErrorToast(message: String) {
        Dialogs.showErrorDialog(this, message)
    }


    //************************************************************

    open fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    //**************************************************************
    fun isConected(): Boolean {
        return if (NetworkUtils.isConnected()) {

            true
        } else {
            noConection()
            false
        }
    }

    private fun noConection() {
        this.showErrorToast(Utility.getString(R.string.no_connection))
    }

    fun showProgress() {
        loaderDialog.show()
    }

    fun hideProgress() {
        loaderDialog.hide()
    }

    //**************************************************************

    fun animateScreenTransition() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    fun <T : Parcelable?> getParcelable(intent: Intent, key: String, className: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(key, className)
        } else {
            intent.getParcelableExtra<T>(key) as T
        }
    }

    fun hide_keyboard() {
        try {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
        }
    }

    fun addHideKeyboardToView(view: View) {
        view.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    hide_keyboard()
                }//Do Something
            }

            v?.onTouchEvent(event) ?: true
        }
    }

}