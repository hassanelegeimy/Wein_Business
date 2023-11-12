package com.wein_business.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.wein_business.data.model.LookupModel
import com.wein_business.data.model.product.ProductAttachmentModel
import com.wein_business.data.model.product.ProviderProductModel
import com.wein_business.data.model.request.CreateUpdateProductRequest
import com.wein_business.data.model.request.ProductAttachmentDeleteRequest
import com.wein_business.data.providers.AssetsProvider
import com.wein_business.data.providers.product.ProviderProductCreateUpdateProvider
import com.wein_business.data.providers.product.ProductAttachmentsProvider
import com.wein_business.data.providers.product.ProviderProductDetailsProvider
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.adapters.ProductAttachmentsAdapter
import com.wein_business.ui.fragment.picker.AttachmentPickerSheetFragment
import com.wein_business.ui.fragment.picker.SpinnerPickerDialogFragment
import com.wein_business.ui.fragment.interfaces.SpinnerPickerListener
import com.wein_business.utils.*
import com.wein_business.utils.interfaces.AlertDialogListener
import com.wein_business.R
import kotlinx.android.synthetic.main.activity_create_update_product.*
import kotlinx.android.synthetic.main.banner_header.*
import java.util.UUID
import kotlin.properties.Delegates

class CreateUpdateProductActivity : GenericActivity(),
    ProviderProductCreateUpdateProvider.Listener,
    ProviderProductDetailsProvider.Listener,
    ProductAttachmentsProvider.Listener,
    SpinnerPickerListener,
    AlertDialogListener {

    private lateinit var providerProductCreateUpdateProvider: ProviderProductCreateUpdateProvider
    private lateinit var providerProductDetailsProvider: ProviderProductDetailsProvider
    private lateinit var productAttachmentsProvider: ProductAttachmentsProvider
    lateinit var attachmentPickerSheetFragment: AttachmentPickerSheetFragment

    private var categoriesList: ArrayList<LookupModel> = arrayListOf()
    private var citiesList: ArrayList<LookupModel> = arrayListOf()
    private var selectedCategory: LookupModel? = null
    private var selectedCity: LookupModel? = null
    private var selectedCoordinates: LatLng? = null

    private var attachmentsList: ArrayList<ProductAttachmentModel> = arrayListOf()
    private val EXTRA_IMAGES_COUNT = 3

    private var productId by Delegates.notNull<Int>()
    lateinit var providerProductModel: ProviderProductModel
    private var isCreationMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_product)

        providerProductCreateUpdateProvider = ProviderProductCreateUpdateProvider(this)
        providerProductDetailsProvider = ProviderProductDetailsProvider(this)
        productAttachmentsProvider = ProductAttachmentsProvider(this)
        attachmentPickerSheetFragment = AttachmentPickerSheetFragment(this)


        if (intent.extras != null && intent.extras!!.getInt(Constants.KEY_PRODUCT_ID) != null) {
            productId = intent.extras!!.getInt(Constants.KEY_PRODUCT_ID) //EditMode

            isCreationMode = productId == Constants.NEW_PRODUCT_DEFAULT_VALUE
        } else {
            finish()
        }

        bindUI()
        addBackPressed()
        loadData()
    }

    private fun bindUI() {
        tvTitle_header.text = Utility.getString(R.string.create_update_product_title)
        RVAttachments_create_update_product.layoutManager = GridLayoutManager(this, 3);

        showNewStatus()//Creation mode by default
        hideAttachments()//Creation mode by default
        btnSubmitProduct_create_update_product.visibility = View.GONE
        btnShowTrips_create_update_product.visibility = View.GONE
    }

    //**************************************************************
    //******Listeners***********************************************

    fun onClick(view: View) {
        when (view) {
            btnBack_header -> onBackPressedDispatcher.onBackPressed()
            spinnerCategory_create_update_product -> {
                SpinnerPickerDialogFragment().show(
                    this, this, categoriesList, Utility.getString(R.string.product_category), spinnerCategory_create_update_product
                )
            }
            spinnerCity_create_update_product -> {
                SpinnerPickerDialogFragment().show(
                    this, this, citiesList, Utility.getString(R.string.city), spinnerCity_create_update_product
                )
            }
            btnLocation_create_update_product -> {
                if (DeviceUtils.checkPlayServices()) {
                    activityMapsPicker(
                        mapResult, if (selectedCoordinates != null) selectedCoordinates!!.latitude.toString() + "," + selectedCoordinates!!.longitude
                        else ""
                    )
                } else this.showErrorToast(getString(R.string.error_google_play_services))

            }
            btnCreateUpdate_create_update_product -> createUpdateProduct()
            btnSubmitProduct_create_update_product -> submitProduct()
            btnShowTrips_create_update_product -> activityProviderProductTrips(productId)
        }
    }

    private fun addBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                animateScreenTransition()
            }
        })
    }

    override fun onSpinnerPickerSelected(lookupSpinner: View, lookupModel: LookupModel) {
        when (lookupSpinner) {
            spinnerCategory_create_update_product -> {
                selectedCategory = lookupModel
                spinnerCategory_create_update_product.text = lookupModel.toString()
            }
            spinnerCity_create_update_product -> {
                selectedCity = lookupModel
                spinnerCity_create_update_product.text = lookupModel.toString()
            }
        }
    }

    private val mapResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null && result.data!!.getStringExtra(Constants.KEY_SELECTED_COORDINATES) != null) {
                var latLng = result.data!!.getStringExtra(Constants.KEY_SELECTED_COORDINATES)!!
                var latLngSplit = latLng.split(",")
                selectedCoordinates = LatLng(latLngSplit[0].toDouble(), latLngSplit[1].toDouble());
                btnLocation_create_update_product.text = latLng
            }
        }
    }

    //***********************************************************************************
    //***********************************************************************************

    private fun bindUIData() {

        tfTitle_create_update_product.editText!!.setText(providerProductModel.title)
        tfDescription_create_update_product.editText!!.setText(providerProductModel.description)

        for (model in categoriesList) {
            if (model.id == providerProductModel.category.id)
                onSpinnerPickerSelected(spinnerCategory_create_update_product, model)
        }

        for (model in citiesList) {
            if (model.id == providerProductModel.city.id)
                onSpinnerPickerSelected(spinnerCity_create_update_product, model)
        }

        selectedCoordinates = LatLng(providerProductModel.latitude, providerProductModel.longitude);
        btnLocation_create_update_product.text = "${providerProductModel.latitude},${providerProductModel.longitude}"


        var isEditableStatus = Utility.isProductStatusAllowEditMode(providerProductModel.status)
        applyEditMode(isEditableStatus)

        editStatus()
        showAttachments(isEditableStatus)
    }

    private fun applyEditMode(isEditableStatus: Boolean) {
        if (isEditableStatus) {
            tfTitle_create_update_product.editText!!.isEnabled = true
            tfDescription_create_update_product.editText!!.isEnabled = true
            spinnerCategory_create_update_product.isEnabled = true
            spinnerCity_create_update_product.isEnabled = true
            btnLocation_create_update_product.isEnabled = true

            btnCreateUpdate_create_update_product.visibility = View.VISIBLE
            btnSubmitProduct_create_update_product.visibility = View.VISIBLE
            btnShowTrips_create_update_product.visibility = View.GONE
        } else {
            tfTitle_create_update_product.editText!!.isEnabled = false
            tfDescription_create_update_product.editText!!.isEnabled = false
            spinnerCategory_create_update_product.isEnabled = false
            spinnerCity_create_update_product.isEnabled = false
            btnLocation_create_update_product.isEnabled = false

            btnCreateUpdate_create_update_product.visibility = View.GONE
            btnSubmitProduct_create_update_product.visibility = View.GONE
            btnShowTrips_create_update_product.visibility = View.VISIBLE
        }
    }

    private fun showNewStatus(){
        tvStatus_create_update_product.text =getString(R.string.product_status_new)
        tvStatusComment_create_update_product.visibility = View.GONE
    }

    private fun editStatus() {
        tvStatus_create_update_product.text = providerProductModel.status.statusLookup.toString()
        if (providerProductModel.status.showComment) {
            tvStatusComment_create_update_product.text = providerProductModel.status.adminComment
            tvStatusComment_create_update_product.visibility = View.VISIBLE
        } else {
            tvStatusComment_create_update_product.visibility = View.GONE
        }
    }

    private fun showAttachments(isEditableStatus: Boolean) {
        attachmentsList.clear()
        layoutAttachments_create_update_product.visibility = View.VISIBLE

        if (!providerProductModel.videos.isNullOrEmpty()) {
            attachmentsList.add(Utility.getProductVideoObject(providerProductModel.videos[0]))
        } else {
            attachmentsList.add(Utility.getProductVideoObject(""))
        }

        if(!providerProductModel.imageMaster.isNullOrEmpty()){
            attachmentsList.add(Utility.getProductImageMasterObject(providerProductModel.imageMaster))
        }else{
            attachmentsList.add(Utility.getProductImageMasterObject(""))
        }

        for (image in providerProductModel.images){
            attachmentsList.add(Utility.getProductImageObject(image))
        }

        for (i in providerProductModel.images.size until EXTRA_IMAGES_COUNT) {
            attachmentsList.add(Utility.getProductImageObject(""))
        }

        RVAttachments_create_update_product.adapter = ProductAttachmentsAdapter(this, attachmentsList, attachmentPickerSheetFragment, isEditableStatus)
    }

    private fun hideAttachments() {
        layoutAttachments_create_update_product.visibility = View.GONE
    }

    //***********************************************************************************
    //***********************************************************************************

    private fun loadData() {
        //TODO static lookups
        if (citiesList.isEmpty())
            citiesList = AssetsProvider.getCitiesList()
        if (categoriesList.isEmpty())
            categoriesList = AssetsProvider.getCategoriesList()

        if (!isCreationMode && isConected()) {// load details in EditMode
            showProgress()
            getProviderProductDetails()
        }
    }

    private fun getProviderProductDetails() {
        providerProductDetailsProvider.getProviderProductDetails(productId)
    }

    fun uploadProductAttachment(position: Int, attachmentType: String, uri: Uri) {
        var attachments = Utility.getFileMultipart(
            FileUtils.fileFromContentUri(
                this, uri, UUID.randomUUID().toString()
            )
        )

        showProgress()
        productAttachmentsProvider.uploadProductAttachment(
            position,
            Utility.createPartFromString("$productId"),
            Utility.createPartFromString(attachmentType),
            attachments
        )
    }

    fun deleteProductAttachment(position: Int, productAttachmentModel: ProductAttachmentModel) {
        showProgress()
        productAttachmentsProvider.deleteProductAttachment(
            position, ProductAttachmentDeleteRequest(productId, productAttachmentModel.fileId!!)
        )
    }

    private fun createUpdateProduct() {
        var request = getUpdateRequest() ?: return

        showProgress()
        providerProductCreateUpdateProvider.createUpdateProduct(
            request
        )
    }

    private fun submitProduct() {
        Dialogs.showAlertDialog(this, this, Constants.ALERT_SUBMIT_PRODUCT, getString(R.string.alert_submit_product))
    }

    private fun getUpdateRequest(): CreateUpdateProductRequest? {
        //TODO Validation of fields
        if (selectedCity == null || selectedCategory == null || selectedCoordinates == null) {
            this.showErrorToast("enter all fields")
            return null
        }

        var title = tfTitle_create_update_product.editText!!.text.toString()
        var description = tfDescription_create_update_product.editText!!.text.toString()
        var activityCategoryId = selectedCategory!!.id
        var cityId = selectedCity!!.id
        var latitude = selectedCoordinates!!.latitude
        var longitude = selectedCoordinates!!.longitude

        return CreateUpdateProductRequest(
            id = productId,
            title = title,
            description = description,
            activityCategoryId = activityCategoryId,
            cityId = cityId,
            latitude = latitude,
            longitude = longitude
        )
    }


    //***********************************************************************************
    //***********************************************************************************
    override fun onAlertDialogConfirm(alertType: String) {
        if (alertType == Constants.ALERT_SUBMIT_PRODUCT) {

            var request = getUpdateRequest() ?: return

            showProgress()
            providerProductCreateUpdateProvider.submitProduct(
                request
            )
        }
    }

    override fun onAlertDialogCancel(alertType: String) {

    }

    override fun onCreateUpdateProductSuccess(message: String, providerProductModel: ProviderProductModel) {
        hideProgress()

        showSuccessToast(message)
        isCreationMode = false
        productId = providerProductModel.id
        this.providerProductModel = providerProductModel
        bindUIData()

        SessionManager.isNewProductCreatedUpdated = true
    }

    override fun onCreateUpdateProductFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

    override fun onProviderProductDetailsSuccess(providerProductModel: ProviderProductModel) {
        hideProgress()
        productId = providerProductModel.id
        this.providerProductModel = providerProductModel
        bindUIData()
    }

    override fun onProviderProductDetailFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

    //*******Attachments
    override fun onProductUploadAttachmentSuccess(position: Int, productAttachmentModel: ProductAttachmentModel) {
        hideProgress()

        attachmentsList[position].fileId = productAttachmentModel.fileId

        RVAttachments_create_update_product.adapter!!.notifyDataSetChanged()
        SessionManager.isNewProductCreatedUpdated = true
    }

    override fun onProductUploadAttachmentFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

    override fun onProductDeleteAttachmentSuccess(position: Int) {
        hideProgress()

        attachmentsList[position].fileId = ""

        RVAttachments_create_update_product.adapter!!.notifyDataSetChanged()
        SessionManager.isNewProductCreatedUpdated = true
    }

    override fun onProductDeleteAttachmentFail(message: String) {
        hideProgress()
        showApiErrorToast(message)
    }

}