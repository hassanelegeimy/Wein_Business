package com.wein_business.utils

object Constants {

    //************************ Main ***************************
    const val APP_PLATFORM = "ANDROID"

    //********************** SESSION ***************************
    private const val BASE_URL_STAGING = "http://68.178.167.129:8081/wein/api/"
    private const val BASE_URL_PRODUCTION = "http://68.178.167.129:8081/wein/api/"

    private const val isProductionEnvironment //= true       //PRODUCTION
            = false      //STAGING

    fun getBaseURL(): String? {
        return if (isProductionEnvironment) BASE_URL_PRODUCTION else BASE_URL_STAGING
    }
    //********************** URLs ***************************
    var downloadFileUrl = getBaseURL()+"public/GoogleDrive/downloadFile"

    //******************* Rest Headers  *********************
    const val HEADER_AUTH = "Authorization"
    const val HEADER_BEARER = "Bearer "

    //*********************** General **************************
    const val TERMS_CONDITIONS_URL = "https://www.termsfeed.com/blog/sample-terms-and-conditions-template/"

    const val SPINNER_DEFAULT_VALUE = 1
    const val NEW_PRODUCT_DEFAULT_VALUE = -1


    //*********************** PERMISSIONS **************************
    const val CAMERA_PERMISSION_REQUEST_CODE = 1

    //********** Intent transition keys
    const val KEY_IMAGES_LIST = "KEY_IMAGES_LIST"
    const val KEY_IMAGE_POSITION = "KEY_IMAGE_POSITION"
    const val KEY_SELECTED_COORDINATES = "KEY_SELECTED_COORDINATES"
    const val KEY_SCANNED_QR = "KEY_SCANNED_QR"

    const val KEY_PRODUCT_ID = "KEY_PRODUCT_ID"
    const val KEY_VIDEO_URL = "KEY_VIDEO_URL"

    const val QR_SCANNER_RESULT = "QR_SCANNER_RESULT"
    //****** Permission
//    const val PERMISSION_EXTERNAL_STORAGE = 1001

    //********************** Preferences **************************
    const val PREFERENCES_NAME = "WEEEIN_BUSINESS"
    const val KEY_LANG = "KEY_LANG"
    const val KEY_USER_MODEL = "KEY_USER_MODEL"
    const val KEY_PROVIDER_STATUS_MODEL = "KEY_PROVIDER_STATUS_MODEL"

    const val LOCALE_DEFAULT = "ar"
    const val LOCALE_ARABIC = "ar"
    const val LOCALE_ENGLISH = "en"

    //******************** Alert Dialog Confirmation *********************************
    const val ALERT_SUBMIT_PROFILE = "ALERT_SUBMIT_PROFILE"
    const val ALERT_SUBMIT_PRODUCT = "ALERT_SUBMIT_PRODUCT"
    //******************** Constants *********************************
    const val SKELETON_TIME: Long = 1000
    const val OTP_RESEND_TIME: Long = 120
    const val TOAST_LENGTH: Long = 3000  //Important

    const val TRIP_PRICE_CLASS_STANDARD = 1
}