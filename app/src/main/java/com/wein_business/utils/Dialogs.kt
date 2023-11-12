package com.wein_business.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.*
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.fragment.picker.DatePickerRangeFragment
import com.wein_business.utils.Constants.TOAST_LENGTH
import com.wein_business.utils.interfaces.AlertDialogListener
import java.util.*


object Dialogs {

    //*************************************************************************************
    //****** Loader *****************************************************************
    fun loaderDialog(context: Context): Dialog {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loader)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    //******************************************************************************
    //****** Toast *****************************************************************

    fun showToast(activity: GenericActivity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccessDialog(activity: GenericActivity, message: String) {
        showDialog(activity, message, R.drawable.ic_success)
    }

    fun showAlertDialog(activity: GenericActivity, message: String) {
        showDialog(activity, message, R.drawable.ic_alert)
    }

    fun showErrorDialog(activity: GenericActivity, message: String) {
        showDialog(activity, message, R.drawable.ic_fail)
    }

    private fun showDialog(activity: GenericActivity, message: String, icon: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_toast)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.width = (Utility.getDeviceMetrics(activity)!!.widthPixels - 100)
        val image_dialogToast = dialog.findViewById<View>(R.id.image_dialogToast) as ImageView
        val tv_dialogToast = dialog.findViewById<View>(R.id.tv_dialogToast) as TextView
        tv_dialogToast.text = message
        image_dialogToast.setImageResource(icon)
        image_dialogToast.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.hide()
        }, TOAST_LENGTH);
    }


    //*************************************************************************************
    //******Alert Dialogs *****************************************************************

    fun showAlertDialog(
        context: Context,
        alertDialogListener: AlertDialogListener,
        alertType: String,
        message: String
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_alert)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.width = (Utility.getDeviceMetrics(context)!!.widthPixels - 100)

        val tv_dialogAlert = dialog.findViewById<View>(R.id.tv_dialogAlert) as TextView
        val btnAccept_dialogAlert = dialog.findViewById<View>(R.id.btnAccept_dialogAlert) as Button
        val btnCancel_dialogAlert = dialog.findViewById<View>(R.id.btnCancel_dialogAlert) as Button

        tv_dialogAlert.text = message

        btnAccept_dialogAlert.setOnClickListener {
            alertDialogListener.onAlertDialogConfirm(alertType)
            dialog.dismiss()
        }

        btnCancel_dialogAlert.setOnClickListener {
            alertDialogListener.onAlertDialogCancel(alertType)
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }


    //*************************************************************************************
    //******Date and Time Dialogs *****************************************************************

    fun showGregorianDatePickerRange(
        activity: GenericActivity,
        targetEditText: EditText,
        relatedEditText: EditText,
        todayIsMin: Boolean,
        todayIsMaximum: Boolean,
        isStartDate: Boolean
    ) {
        val datePicker = DatePickerRangeFragment()
        datePicker.setMinMax(todayIsMin, todayIsMaximum, isStartDate)
        datePicker.setTargetEditText(targetEditText, relatedEditText)
        datePicker.show(activity.supportFragmentManager, "datePicker")
    }


    fun showTimePicker(activity: GenericActivity, editText: EditText) {
        val picker = TimePickerDialog(
            activity,
            { _, sHour, sMinute ->
                editText.setText(
                    String.format(
                        Locale.ENGLISH,
                        "%02d:%02d",
                        sHour,
                        sMinute
                    )
                )
            }, 8, 0, true
        )
        picker.show()
    }

}