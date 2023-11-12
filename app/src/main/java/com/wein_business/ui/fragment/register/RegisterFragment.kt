package com.wein_business.ui.fragment.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wein_business.R
import com.wein_business.data.model.request.authorize.RegisterRequest
import com.wein_business.data.model.authorize.OtpModel
import com.wein_business.data.providers.authorize.RegisterProvider
import com.wein_business.ui.activity.RegisterActivity
import com.wein_business.ui.fragment.generic.GenericFragment
import com.wein_business.utils.Constants
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : GenericFragment(),
    View.OnClickListener,
    RegisterProvider.Listener {

    private lateinit var activity: RegisterActivity
    private lateinit var registerProvider: RegisterProvider

    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerProvider = RegisterProvider(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as RegisterActivity
        activity.registerFragment = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        listeners()
    }

    fun bindUI() {

    }

    private fun listeners() {
        btnVerifyMobile_register.setOnClickListener(this)
        btnShowTerms_register.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnVerifyMobile_register -> register()
            btnShowTerms_register -> Utility.openExternalUrl(
                activity,
                Constants.TERMS_CONDITIONS_URL
            )
        }
    }

    //**********************************************************
    //**********************************************************

    private fun register() {
        //TODO VALIDATION
        var mobileNumber = tfVMobile_register.editText?.text.toString()
        password = tfPassword_register.editText?.text.toString()

        if (!cb_accept.isChecked) {
            activity.showErrorToast(Utility.getString(R.string.error_terms))
            return
        }

        //TODO REMOVE EXTRA PARAMETERS
        activity.showProgress()
        registerProvider.register(
            RegisterRequest(
                activity.userType,
                "GENDER_MALE",//TODO REMOVE
                "",
                mobileNumber,
                password
            )
        )
    }

    //**********************************************************
    //**********************************************************
    override fun onRegisterSuccess(otpModel: OtpModel) {
        activity.hideProgress()
        activity.saveTempData(password, otpModel.id)
    }

    override fun onRegisterFail(message: String) {
        activity.hideProgress()
        activity.showApiErrorToast(message)
    }

}