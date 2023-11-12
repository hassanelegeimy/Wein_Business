package com.wein_business.utils.interfaces

interface AlertDialogListener {
    fun onAlertDialogConfirm(alertType:String)
    fun onAlertDialogCancel(alertType:String)
}