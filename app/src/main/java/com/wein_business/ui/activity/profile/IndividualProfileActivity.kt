package com.wein_business.ui.activity.profile

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wein_business.R
import com.wein_business.data.model.LookupModel
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.model.profile.IndividualProfileModel
import com.wein_business.data.model.profile.ProfileAttachmentModel
import com.wein_business.data.model.request.UpdateProfileIndividualRequest
import com.wein_business.data.providers.AssetsProvider
import com.wein_business.data.providers.profile.IndividualProfileProvider
import com.wein_business.data.providers.profile.IndividualProfileUpdateProvider
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
import kotlinx.android.synthetic.main.activity_individual_profile.*
import kotlinx.android.synthetic.main.banner_header.*

class IndividualProfileActivity : GenericActivity(),
    IndividualProfileProvider.Listener,
    IndividualProfileUpdateProvider.Listener,
    UploadProfileAttachmentsProvider.Listener,
    SpinnerPickerListener,
    SwipeRefreshLayout.OnRefreshListener,
    AlertDialogListener {

    private lateinit var individualProfileProvider: IndividualProfileProvider
    private lateinit var individualProfileUpdateProvider: IndividualProfileUpdateProvider
    private lateinit var uploadProfileAttachmentsProvider: UploadProfileAttachmentsProvider

    lateinit var attachmentPickerSheetFragment: AttachmentPickerSheetFragment
    lateinit var profileAttachmentsAdapter: ProfileAttachmentsAdapter

    private lateinit var individualProfileModel: IndividualProfileModel
    private lateinit var citiesList: ArrayList<LookupModel>
    private var selectedCity: LookupModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_profile)

        individualProfileProvider = IndividualProfileProvider(this)
        individualProfileUpdateProvider = IndividualProfileUpdateProvider(this)
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
        swipeRefresh_individual_profile.setOnRefreshListener(this)
    }

    private fun bindUIData() {
        var isEditableStatus = Utility.isProviderStatusAllowEditMode(individualProfileModel.userStatus)
        applyEditMode(isEditableStatus)

        tfName_individual_profile.editText!!.setText(individualProfileModel.individualName)
        tfIdNumber_individual_profile.editText!!.setText(individualProfileModel.idNumber)
        tfIban_individual_profile.editText!!.setText(individualProfileModel.iban)
        tfMobile_individual_profile.editText!!.setText(individualProfileModel.mobile)
        tfEmail_individual_profile.editText!!.setText(individualProfileModel.email)

        for (model in citiesList) {
            if (model.id == individualProfileModel.cityId)
                onSpinnerPickerSelected(spinnerCity_individual_profile, model)
        }


        RVAttachments_individual_profile.layoutManager =
            GridLayoutManager(this, 2);

        profileAttachmentsAdapter =
            ProfileAttachmentsAdapter(
                this,
                individualProfileModel.attachments,
                attachmentPickerSheetFragment,
                uploadProfileAttachmentsProvider,
                isEditableStatus
            );
        RVAttachments_individual_profile.adapter = profileAttachmentsAdapter
        RVAttachments_individual_profile.setHasFixedSize(false);

        //*************** Status
        tvPercentage_individual_profile.text =
            getString(R.string.profile_percentage_title) + " ${individualProfileModel.completedPercent} %"
        tvStatus_individual_profile.text = individualProfileModel.userStatus.userStatusLookup.toString()

        if (individualProfileModel.userStatus.showComment) {
            tvStatusComment_individual_profile.text = individualProfileModel.userStatus.adminComment
            tvStatusComment_individual_profile.visibility = View.VISIBLE
        } else {
            tvStatusComment_individual_profile.visibility = View.GONE
        }

        if (individualProfileModel.completedPercent == 100) {
            tvPercentage_individual_profile.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_checked_on,
                0
            );
        }
        //***************

    }

    private fun applyEditMode(isEditableStatus: Boolean) {
        if (isEditableStatus) {
            tfName_individual_profile.editText!!.isEnabled = true
            tfIdNumber_individual_profile.editText!!.isEnabled = true
            tfIban_individual_profile.editText!!.isEnabled = true
            spinnerCity_individual_profile!!.isEnabled = true
            tfMobile_individual_profile.editText!!.isEnabled = true
            tfEmail_individual_profile.editText!!.isEnabled = true
            layoutActions_individual_profile.visibility = View.VISIBLE
        } else {
            tfName_individual_profile.editText!!.isEnabled = false
            tfIdNumber_individual_profile.editText!!.isEnabled = false
            tfIban_individual_profile.editText!!.isEnabled = false
            spinnerCity_individual_profile!!.isEnabled = false
            tfMobile_individual_profile.editText!!.isEnabled = false
            tfEmail_individual_profile.editText!!.isEnabled = false
            layoutActions_individual_profile.visibility = View.GONE
        }

    }
    //**************************************************************
    //******Listeners***********************************************

    fun onClick(view: View) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
            spinnerCity_individual_profile -> {
                SpinnerPickerDialogFragment().show(
                    this,
                    this,
                    citiesList,
                    Utility.getString(R.string.city),
                    spinnerCity_individual_profile
                )
            }

            btnUpdate_individual_profile -> updateProfile()
            btnSubmit_individual_profile -> submitProfile()
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
        spinnerCity_individual_profile.text = lookupModel.toString()
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
            getIndividualProfile()
        }
    }

    private fun getIndividualProfile() {
        individualProfileProvider.getIndividualProfile()
    }

    private fun updateProfile() {

        showProgress()
        individualProfileUpdateProvider.updateIndividualProfile(
            getUpdateRequest()
        )
    }

    private fun submitProfile() {
        Dialogs.showAlertDialog(this, this, Constants.ALERT_SUBMIT_PROFILE, getString(R.string.alert_submit_profile))
    }

    private fun getUpdateRequest(): UpdateProfileIndividualRequest {
        var individualName = tfName_individual_profile.editText!!.text.toString()
        var idNumber = tfIdNumber_individual_profile.editText!!.text.toString()
        var iban = tfIban_individual_profile.editText!!.text.toString()
        var email = tfEmail_individual_profile.editText!!.text.toString()

        var cityId: Int = if (selectedCity != null) {
            selectedCity!!.id
        } else
            SPINNER_DEFAULT_VALUE


        return UpdateProfileIndividualRequest(
            individualName = individualName,
            idNumber = idNumber,
            iban = iban,
            cityId = cityId,
            email = email
        )
    }

    //***********************************************************************************
    //***********************************************************************************
    override fun onAlertDialogConfirm(alertType: String) {
        if (alertType == Constants.ALERT_SUBMIT_PROFILE) {
            showProgress()
            individualProfileUpdateProvider.submitIndividualProfile(
                getUpdateRequest()
            )
        }
    }

    override fun onAlertDialogCancel(alertType: String) {

    }

    override fun onIndividualProfileSuccess(individualProfileModel: IndividualProfileModel) {
        hideProgress()
        swipeRefresh_individual_profile.isRefreshing = false

        this.individualProfileModel = individualProfileModel
        SessionManager.changeProviderStatusModel(individualProfileModel.userStatus)

        bindUIData()
    }

    override fun onIndividualProfileFail(message: String) {
        hideProgress()
        swipeRefresh_individual_profile.isRefreshing = false

        showApiErrorToast(message)
    }

    override fun onIndividualUpdateProfileSuccess(
        message: String,
        individualProfileModel: IndividualProfileModel
    ) {
        hideProgress()
        this.showSuccessToast(message)

        var userModel: UserModel = SessionManager.userModel
        userModel.displayName = individualProfileModel.individualName
        userModel.email = individualProfileModel.email

        SessionManager.changeProviderStatusModel(individualProfileModel.userStatus)
        SessionManager.changeSessionUserModel(userModel)

        this.individualProfileModel = individualProfileModel
        scroll_individual_profile.smoothScrollTo(0, 0)
        bindUIData()
    }

    override fun onIndividualUpdateProfileFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }


    override fun onProfileUploadAttachmentSuccess(position: Int, profileAttachmentModel: ProfileAttachmentModel) {
        hideProgress()
        individualProfileModel.attachments[position] = profileAttachmentModel
        profileAttachmentsAdapter.notifyDataSetChanged()
    }

    override fun onProfileUploadAttachmentFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

}