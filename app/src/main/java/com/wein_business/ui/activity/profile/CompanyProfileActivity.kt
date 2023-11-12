package com.wein_business.ui.activity.profile

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wein_business.R
import com.wein_business.data.model.LookupModel
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.model.profile.CompanyProfileModel
import com.wein_business.data.model.profile.ProfileAttachmentModel
import com.wein_business.data.model.request.UpdateProfileCompanyRequest
import com.wein_business.data.providers.AssetsProvider
import com.wein_business.data.providers.profile.CompanyProfileProvider
import com.wein_business.data.providers.profile.CompanyProfileUpdateProvider
import com.wein_business.data.providers.profile.UploadProfileAttachmentsProvider
import com.wein_business.ui.fragment.interfaces.SpinnerPickerListener
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.adapters.ProfileAttachmentsAdapter
import com.wein_business.ui.fragment.picker.AttachmentPickerSheetFragment
import com.wein_business.ui.fragment.picker.SpinnerPickerDialogFragment
import com.wein_business.utils.Constants
import com.wein_business.utils.Constants.SPINNER_DEFAULT_VALUE
import com.wein_business.utils.Dialogs
import com.wein_business.utils.SessionManager
import com.wein_business.utils.Utility
import com.wein_business.utils.interfaces.AlertDialogListener
import kotlinx.android.synthetic.main.activity_company_profile.*
import kotlinx.android.synthetic.main.banner_header.*

class CompanyProfileActivity : GenericActivity(),
    CompanyProfileProvider.Listener,
    CompanyProfileUpdateProvider.Listener,
    UploadProfileAttachmentsProvider.Listener,
    SpinnerPickerListener,
    SwipeRefreshLayout.OnRefreshListener,
    AlertDialogListener {

    private lateinit var companyProfileProvider: CompanyProfileProvider
    private lateinit var companyProfileUpdateProvider: CompanyProfileUpdateProvider
    private lateinit var uploadProfileAttachmentsProvider: UploadProfileAttachmentsProvider

    lateinit var attachmentPickerSheetFragment: AttachmentPickerSheetFragment
    lateinit var profileAttachmentsAdapter: ProfileAttachmentsAdapter

    private lateinit var companyProfileModel: CompanyProfileModel
    private lateinit var citiesList: ArrayList<LookupModel>
    private var selectedCity: LookupModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)

        companyProfileProvider = CompanyProfileProvider(this)
        companyProfileUpdateProvider = CompanyProfileUpdateProvider(this)
        uploadProfileAttachmentsProvider = UploadProfileAttachmentsProvider(this)

        //Attachment picker init
        attachmentPickerSheetFragment = AttachmentPickerSheetFragment(this)

        bindUI()
        initListeners()
        addBackPressed()
        loadData()
    }


    private fun bindUI() {
        tvTitle_header.text = Utility.getString(R.string.menu_profile)

    }

    private fun initListeners() {
        swipeRefresh_company_profile.setOnRefreshListener(this)
    }

    private fun bindUIData() {
        var isEditableStatus = Utility.isProviderStatusAllowEditMode(companyProfileModel.userStatus)
        applyEditMode(isEditableStatus)

        tfName_company_profile.editText!!.setText(companyProfileModel.companyName)
        tfCR_company_profile.editText!!.setText(companyProfileModel.crNumber)
        tfIban_company_profile.editText!!.setText(companyProfileModel.iban)
        tfMobile_company_profile.editText!!.setText(companyProfileModel.mobile)
        tfEmail_company_profile.editText!!.setText(companyProfileModel.email)

        for (model in citiesList) {
            if (model.id == companyProfileModel.cityId)
                onSpinnerPickerSelected(spinnerCity_company_profile, model)
        }

        RVAttachments_company_profile.layoutManager =
            GridLayoutManager(this, 2);

        profileAttachmentsAdapter =
            ProfileAttachmentsAdapter(
                this,
                companyProfileModel.attachments,
                attachmentPickerSheetFragment,
                uploadProfileAttachmentsProvider,
                isEditableStatus
            );
        RVAttachments_company_profile.adapter = profileAttachmentsAdapter
        RVAttachments_company_profile.setHasFixedSize(false);

        //*************** Status
        tvPercentage_company_profile.text =
            getString(R.string.profile_percentage_title) + " ${companyProfileModel.completedPercent} %"
        tvStatus_company_profile.text = companyProfileModel.userStatus.userStatusLookup.toString()

        if (companyProfileModel.userStatus.showComment) {
            tvStatusComment_company_profile.text = companyProfileModel.userStatus.adminComment
            tvStatusComment_company_profile.visibility = View.VISIBLE
        } else {
            tvStatusComment_company_profile.visibility = View.GONE
        }

        if (companyProfileModel.completedPercent == 100) {
            tvPercentage_company_profile.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_checked_on,
                0
            );
        }
    }

    private fun applyEditMode(isEditableStatus: Boolean) {
        if (isEditableStatus) {
            tfName_company_profile.editText!!.isEnabled = true
            tfCR_company_profile.editText!!.isEnabled = true
            tfIban_company_profile.editText!!.isEnabled = true
            spinnerCity_company_profile!!.isEnabled = true
            tfMobile_company_profile.editText!!.isEnabled = true
            tfEmail_company_profile.editText!!.isEnabled = true
            layoutActions_company_profile.visibility = View.VISIBLE
        } else {
            tfName_company_profile.editText!!.isEnabled = false
            tfCR_company_profile.editText!!.isEnabled = false
            tfIban_company_profile.editText!!.isEnabled = false
            spinnerCity_company_profile!!.isEnabled = false
            tfMobile_company_profile.editText!!.isEnabled = false
            tfEmail_company_profile.editText!!.isEnabled = false
            layoutActions_company_profile.visibility = View.GONE
        }
    }

    //**************************************************************
    //******Listeners***********************************************

    fun onClick(view: View) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
            spinnerCity_company_profile -> {
                SpinnerPickerDialogFragment().show(
                    this,
                    this,
                    citiesList,
                    Utility.getString(R.string.city),
                    spinnerCity_company_profile
                )
            }

            btnUpdate_company_profile -> updateProfile()
            btnSubmit_company_profile -> submitProfile()
        }
    }

    private fun addBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activityMain()
            }
        })
    }

    override fun onSpinnerPickerSelected(lookupSpinner: View, lookupModel: LookupModel) {
        selectedCity = lookupModel
        spinnerCity_company_profile.text = lookupModel.toString()
    }

    override fun onRefresh() {
        loadData()
    }
    //***********************************************************************************
    //***********************************************************************************

    private fun loadData() {
        //TODO static lookups
        citiesList = AssetsProvider.getCitiesList()

        if (isConected()) {
            showProgress()
            getCompanyProfile()
        }
    }

    private fun getCompanyProfile() {
        companyProfileProvider.getCompanyProfile()
    }

    private fun updateProfile() {
        showProgress()
        companyProfileUpdateProvider.updateCompanyProfile(
            getUpdateRequest()
        )
    }

    private fun submitProfile() {
        Dialogs.showAlertDialog(this, this, Constants.ALERT_SUBMIT_PROFILE, getString(R.string.alert_submit_profile))
    }

    private fun getUpdateRequest(): UpdateProfileCompanyRequest {
        var companyName = tfName_company_profile.editText!!.text.toString()
        var crNumber = tfCR_company_profile.editText!!.text.toString()
        var iban = tfIban_company_profile.editText!!.text.toString()
        var email = tfEmail_company_profile.editText!!.text.toString()

        var cityId: Int = if (selectedCity != null) {
            selectedCity!!.id
        } else
            SPINNER_DEFAULT_VALUE

        return UpdateProfileCompanyRequest(
            companyName = companyName,
            crNumber = crNumber,
            iban = iban,
            cityId = cityId,
            email = email,
        )
    }

    //***********************************************************************************
    //***********************************************************************************
    override fun onAlertDialogConfirm(alertType: String) {
        if (alertType == Constants.ALERT_SUBMIT_PROFILE) {
            showProgress()
            companyProfileUpdateProvider.submitCompanyProfile(
                getUpdateRequest()
            )
        }
    }

    override fun onAlertDialogCancel(alertType: String) {

    }

    var i = 0
    override fun onCompanyProfileSuccess(companyProfileModel: CompanyProfileModel) {
        hideProgress()
        swipeRefresh_company_profile.isRefreshing = false

        this.companyProfileModel = companyProfileModel
        SessionManager.changeProviderStatusModel(companyProfileModel.userStatus)

        bindUIData()
    }

    override fun onCompanyProfileFail(message: String) {
        hideProgress()
        swipeRefresh_company_profile.isRefreshing = false

        showApiErrorToast(message)
    }

    override fun onCompanyUpdateProfileSuccess(
        message: String,
        companyProfileModel: CompanyProfileModel
    ) {
        hideProgress()
        this.showSuccessToast(message)

        var userModel: UserModel = SessionManager.userModel
        userModel.displayName = companyProfileModel.companyName
        userModel.email = companyProfileModel.email

        SessionManager.changeProviderStatusModel(companyProfileModel.userStatus)
        SessionManager.changeSessionUserModel(userModel)

        this.companyProfileModel = companyProfileModel
        scroll_company_profile.smoothScrollTo(0, 0)
        bindUIData()
    }

    override fun onCompanyUpdateProfileFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

    override fun onProfileUploadAttachmentSuccess(
        position: Int,
        profileAttachmentModel: ProfileAttachmentModel
    ) {
        hideProgress()
        companyProfileModel.attachments[position] = profileAttachmentModel
        profileAttachmentsAdapter.notifyDataSetChanged()
    }

    override fun onProfileUploadAttachmentFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

}