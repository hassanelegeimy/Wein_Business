package com.wein_business.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.adapters.pager.RegisterPagerAdapter
import com.wein_business.ui.fragment.register.RegisterFragment
import com.wein_business.ui.fragment.register.VerifyRegisterOTPFragment
import com.wein_business.utils.Utility
import com.wein_business.utils.enums.UserType
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.banner_header.*

class RegisterActivity : GenericActivity(), OnClickListener {

    private val POSITION_USER_TYPE = 0
    private val POSITION_REGISTER = 1
    private val POSITION_OTP = 2

    private var registerPagerAdapter: RegisterPagerAdapter? = null
    var registerFragment: RegisterFragment? = null
    var verifyRegisterOTPFragment: VerifyRegisterOTPFragment? = null

    //****** To be Filled api fields
    lateinit var userType: String
    lateinit var password: String
    lateinit var otpRequestId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        bindUI()
        addBackPressed()
    }

    private fun bindUI() {
        tvTitle_header.text = Utility.getString(R.string.register_title)
        initPager()
    }

    private fun initPager() {
        registerPagerAdapter = RegisterPagerAdapter(supportFragmentManager, lifecycle)
        pager_register.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager_register.adapter = registerPagerAdapter
        pager_register.currentItem = POSITION_USER_TYPE
        pager_register.offscreenPageLimit = 2
        pager_register.isUserInputEnabled = false
    }

    override fun onClick(view: View?) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun addBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (pager_register.currentItem) {
                    POSITION_USER_TYPE -> {
                        finish()
                        animateScreenTransition()
                    }
                    POSITION_REGISTER -> showOtpUserType()
                    POSITION_OTP -> showRegisterStep()
                }
            }
        })
    }


//***********************************************************************************
//***********************************************************************************

    fun setUserTypeValue(userType: UserType) {
        this.userType = userType.toString()
        registerFragment?.bindUI()
        showRegisterStep()
    }


    fun saveTempData(password: String, otpRequestId: String) {
        this.password = password
        this.otpRequestId = otpRequestId
        showOtpStep()
    }

    //***********************************************************************************
    //***********************************************************************************

    private fun showOtpUserType() {
        pager_register.currentItem = POSITION_USER_TYPE
    }

    fun showRegisterStep() {
        pager_register.currentItem = POSITION_REGISTER
    }

    private fun showOtpStep() {
        pager_register.currentItem = POSITION_OTP
        verifyRegisterOTPFragment?.startCountDown()
    }


}