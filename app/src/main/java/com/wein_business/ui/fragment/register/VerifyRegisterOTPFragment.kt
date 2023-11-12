package com.wein_business.ui.fragment.register

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.wein_business.R
import com.wein_business.data.model.request.authorize.ResendOtpRequest
import com.wein_business.data.model.request.authorize.VerifyOtpRequest
import com.wein_business.data.model.authorize.OtpModel
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.providers.authorize.ResendOtpProvider
import com.wein_business.data.providers.authorize.VerifyRegisterOtpProvider
import com.wein_business.ui.activity.RegisterActivity
import com.wein_business.ui.fragment.generic.GenericFragment
import com.wein_business.utils.Constants
import com.wein_business.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_verify_register_otp.*

class VerifyRegisterOTPFragment : GenericFragment(),
    View.OnClickListener,
    VerifyRegisterOtpProvider.Listener,
    ResendOtpProvider.Listener {

    private lateinit var activity: RegisterActivity
    private lateinit var verifyRegisterOtpProvider: VerifyRegisterOtpProvider
    private lateinit var resendOtpProvider: ResendOtpProvider

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyRegisterOtpProvider = VerifyRegisterOtpProvider(this)
        resendOtpProvider = ResendOtpProvider(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as RegisterActivity
        activity.verifyRegisterOTPFragment = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_verify_register_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        listeners()
    }

    private fun bindUI() {
        initCountDown()
    }

    private fun listeners() {
        btnVerifyOtp.setOnClickListener(this)
        btnResendOtp.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnVerifyOtp -> verifyOtp()
            btnResendOtp -> {
                resendOtp()
            }
        }
    }

    override fun onDetach() {
        //TODO TEST
        super.onDetach()
        if (countDownTimer != null)
            countDownTimer.cancel()
    }
    //**********************************************************
    //**********************************************************

    private fun initCountDown() {
        countDownTimer = object : CountDownTimer(Constants.OTP_RESEND_TIME * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvOtpTimer.text = "${
                    (millisUntilFinished / 1000 / 60).toString().padStart(2, '0')
                } : ${(millisUntilFinished / 1000 % 60).toString().padStart(2, '0')}"
            }

            override fun onFinish() {
                showResendButton()
                tvOtpTimer.text = "00.00"
            }
        }
    }

    fun startCountDown() {
        pinViewOtp.setText("")
        showTimerLayout()
        countDownTimer.start()
    }

    private fun showTimerLayout() {
        btnResendOtp.visibility = GONE
        layoutOtpTimer.visibility = VISIBLE
    }

    private fun showResendButton() {
        btnResendOtp.visibility = VISIBLE
        layoutOtpTimer.visibility = GONE
    }

    //**********************************************************
    //**********************************************************

    private fun verifyOtp() {
        var otp = pinViewOtp.text.toString()
        //TODO VALIDATION

        activity.showProgress()
        verifyRegisterOtpProvider.verifyRegisterOTP(
            VerifyOtpRequest(
                otp,
                activity.otpRequestId,
                activity.password
            )
        )
    }

    private fun resendOtp() {
        activity.showProgress()
        resendOtpProvider.resendOTP(ResendOtpRequest(activity.otpRequestId))
    }
    //**********************************************************
    //**********************************************************


    override fun onVerifyOtpSuccess(userModel: UserModel) {
        activity.hideProgress()

        SessionManager.changeSessionUserModel(userModel)
        activity.activityMain()
    }

    override fun onVerifyOtpFail(message: String) {
        activity.hideProgress()

        activity.showApiErrorToast(message)
    }

    override fun onResendOtpSuccess(otpModel: OtpModel, message: String) {
        activity.hideProgress()

        startCountDown()
    }

    override fun onResendOtpFail(message: String) {
        activity.hideProgress()

        activity.showApiErrorToast(message)
    }

}