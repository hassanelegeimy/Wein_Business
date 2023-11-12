package com.wein_business.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import com.wein_business.R
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.model.request.authorize.LoginRequest
import com.wein_business.data.providers.authorize.LoginProvider
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Dialogs
import com.wein_business.utils.SessionManager
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.banner_header.*

class LoginActivity : GenericActivity(),
    LoginProvider.Listener {

    private lateinit var loginProvider: LoginProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginProvider = LoginProvider(this)


        bindUI()
        listeners()
        addBackPressed()
    }

    private fun bindUI() {
        tvTitle_header.text = Utility.getString(R.string.login)
    }

    private fun listeners() {
        addHideKeyboardToView(layoutMain_login)

        tfPassword_login.editText!!.imeOptions =
            tfPassword_login.editText!!.imeOptions or EditorInfo.IME_ACTION_DONE
        tfPassword_login.editText!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btn_login.performClick()
            }
            false
        }
    }


    fun onClick(view: View) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
            btnNewUser_login -> activityRegistration()
            btnReset_login -> showAlertToast("ResetPassword")
            btn_login -> login()
        }
    }

    private var doubleBackPressedOnce = false
    private fun addBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackPressedOnce) {
                    Utility.exitApp(this@LoginActivity)
                    return
                }
                doubleBackPressedOnce = true
                Dialogs.showToast(this@LoginActivity, Utility.getString(R.string.exit_message))
                Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressedOnce = false }, 1000)
            }
        })
    }

    //***********************************************************************************
    //***********************************************************************************
    private fun login() {
        //TODO VALIDATION
        var mobileOrEmail = tfMobile_login.editText?.text.toString()
        var password = tfPassword_login.editText?.text.toString()

        showProgress()
        loginProvider.login(
            LoginRequest(mobileOrEmail, password)
        )
    }

    //***********************************************************************************
    //***********************************************************************************
    override fun onLoginSuccess(userModel: UserModel) {
        hideProgress()

        SessionManager.changeSessionUserModel(userModel)
        activityMain()
    }

    override fun onLoginFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }
}