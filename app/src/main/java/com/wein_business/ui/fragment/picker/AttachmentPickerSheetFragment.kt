package com.wein_business.ui.fragment.picker

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wein_business.R
import com.wein_business.ui.fragment.interfaces.AttachmentPickerListener
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.FileUtils
import com.wein_business.utils.PermissionUtils
import kotlinx.android.synthetic.main.fragment_sheet_attachment_picker.*


class AttachmentPickerSheetFragment(private var activity: GenericActivity) :
    BottomSheetDialogFragment(), View.OnClickListener {


    private lateinit var attachmentPickerListener: AttachmentPickerListener
    private val fileUtils: FileUtils? = null
    private val cameraPictureFile: java.io.File? = null
    private val currentCropUri: android.net.Uri? = null
    private val isOnlyImages: kotlin.Boolean = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    fun show(attachmentPickerListener: AttachmentPickerListener) {
        show(activity.supportFragmentManager, AttachmentPickerSheetFragment::class.java.simpleName)
        this.attachmentPickerListener = attachmentPickerListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sheet_attachment_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    fun bindUI() {
        btnGallery_attachment_picker.setOnClickListener(this)
        btnCamera_attachment_picker.setOnClickListener(this)
        btnFile_attachment_picker.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCamera_attachment_picker -> openCamera()
            R.id.btnGallery_attachment_picker -> openGallery()
            R.id.btnFile_attachment_picker -> openFiles()
        }
    }

    //***********************************************************************************************8

    private fun openCamera() {
        dismiss()

        if (PermissionUtils.hasWriteStoragePermission(activity)) {
            //TODO CAMERA
            activity.showAlertToast("سيتم اضافتها لاحقا")
        }else{
            requestStorageWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun openFiles() {
        dismiss()

        val filesIntent = Intent(
            Intent.ACTION_GET_CONTENT,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )

        //TODO MERGE FILES WITH GALLERY IN SAME RESPOSNE
        activityResultGallery.launch(filesIntent)
    }

    private fun openGallery() {
        dismiss()

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )

//            val filesIntent =
//                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            filesIntent.type = "image/*"
//            val chooserIntent = Intent.createChooser(filesIntent, "")
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(filesIntent))
////            startActivityForResult(chooserIntent, GALLERY_REQUEST)

            activityResultGallery.launch(galleryIntent)//"image/*"

    }

    //***********************************************************************************************8
    //    private void openCamera() {
    //        TedPermission.with(genericActivity)
    //                .setPermissionListener(new PermissionListener() {
    //                    @Override
    //                    public void onPermissionGranted() {
    //                        cameraPictureFile = fileUtils.getNewPictureFile();
    //                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUtils.getFileUri(cameraPictureFile));
    //                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    //                    }
    //
    //                    @Override
    //                    public void onPermissionDenied(List<String> deniedPermissions) {
    //
    //                    }
    //                })
    //                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    //                .setDeniedMessage(genericActivity.getResources().getString(R.string.storage_deny_permisssion))
    //                .check();
    //    }

    //****************************************************************************************

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == CAMERA_REQUEST) {
//                val fileLength = cameraPictureFile!!.length()
//                val fileSize = fileLength / 1048576
//                if (fileSize > 20) {
//                    Dialogs.showToastMessage(resources.getString(R.string.fileSizeError))
//                    dismiss()
//                    return
//                }
//                imagePickerChooseListener.cameraResult(cameraPictureFile)
//            } else if (requestCode == FILE_REQUEST && data != null) {
//                val uri = data.data
//                val path: String = fileUtils.getPathURI(uri)
//                val selectedFile = File(path)
//                if (path == null || !selectedFile.exists()) {
//                    Dialogs.showToastMessage(
//                        genericActivity.getResources().getString(R.string.error_attachment)
//                    )
//                    dismiss()
//                    return
//                }
//                if (!isOnlyImages && !Arrays.asList(
//                        FileUtils.getSharedInstance().getAcceptedFilesTypes()
//                    )
//                        .contains(FileUtils.getSharedInstance().getMimeType(selectedFile))
//                ) {
//                    val errorMessage: String = Utility.getString(R.string.error_attachment) +
//                            "\n" +
//                            Utility.getString(R.string.attachments_accepted)
//                    Dialogs.showErrorMessage(errorMessage)
//                    dismiss()
//                    return
//                }
//                val fileLength = selectedFile.length()
//                val fileSize = fileLength / 1048576
//                if (fileSize > 20) {
//                    Dialogs.showToastMessage(resources.getString(R.string.fileSizeError))
//                    dismiss()
//                    return
//                }
//                imagePickerChooseListener.fileResult(selectedFile)
//            }
            }
            dismiss()
        } catch (e: Exception) {
            dismiss()
        }
    }

    //************************************************************
    //*** Activity Results
    private val requestStorageWritePermission =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Do something if permission granted
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(activity, R.string.storage_write_deny_permisssion, Toast.LENGTH_SHORT).show()
            }
        }

    private val activityResultGallery: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if(result.data !=null){
                    val imageUri: Uri = result.data!!.data!!
                    attachmentPickerListener.galleryResult(imageUri)
                }else{
                    activity.showErrorToast("Error in Fileeeeee")
                }

                dismiss()
            }
        }

//    private val activityResultGallery = registerForActivityResult<String, Uri>(ActivityResultContracts.GetContent())
//    {
//            uri ->  attachmentPickerListener.galleryResult(uri)
//    }
}