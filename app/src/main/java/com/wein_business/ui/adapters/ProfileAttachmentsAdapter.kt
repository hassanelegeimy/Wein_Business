package com.wein_business.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wein_business.R
import com.wein_business.data.model.profile.ProfileAttachmentModel
import com.wein_business.data.providers.profile.UploadProfileAttachmentsProvider
import com.wein_business.ui.fragment.interfaces.AttachmentPickerListener
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.fragment.picker.AttachmentPickerSheetFragment
import com.wein_business.utils.FileUtils
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.item_profile_attachment.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ProfileAttachmentsAdapter(
    private val activity: GenericActivity,
    private val itemsList: ArrayList<ProfileAttachmentModel>,
    private var attachmentPickerSheetFragment: AttachmentPickerSheetFragment,
    private val uploadProfileAttachmentsProvider: UploadProfileAttachmentsProvider,
    private val isEditableStatus: Boolean
) : RecyclerView.Adapter<ProfileAttachmentsAdapter.ViewHolder?>(),
    AttachmentPickerListener {

    private var currentUploadPosition by Delegates.notNull<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.item_profile_attachment, parent, false)

        var gridDimension = parent.measuredWidth / 2
        view.layoutParams.width = gridDimension;
        view.layoutParams.height = gridDimension;

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ProfileAttachmentModel = itemsList[position]
        holder.tvName_itemProfileAttachment.text = model.toString()

        //**** File Type Image and Extension ***********************************************************
        if (model.fileId.isEmpty()) {//Empty file
            holder.tvExtension_itemProfileAttachment.visibility = View.GONE

            holder.image_itemProfileAttachment.setImageResource(R.color.loading_view)
        } else if (model.mimeType.contains("image", true)) {//Image
            holder.tvExtension_itemProfileAttachment.visibility = View.GONE

            Glide.with(activity)
                .load(Utility.getDownloadFileUrl(model.fileId))
                .placeholder(R.color.loading_view)
                .error(R.color.loading_view)
                .centerCrop()
                .into(holder.image_itemProfileAttachment)
        } else {//Not Image
            holder.tvExtension_itemProfileAttachment.visibility = View.VISIBLE
            holder.image_itemProfileAttachment.setImageResource(R.color.yellow_dark)

            holder.tvExtension_itemProfileAttachment.text = FileUtils.getFileExtensionFromMimeType(model.mimeType)
        }

        //*** Buttons *********************************************************
        if (model.fileId == "") {
            holder.btnEdit_itemProfileAttachment.setImageResource(R.drawable.ic_add)
        } else {
            holder.btnEdit_itemProfileAttachment.setImageResource(R.drawable.ic_edit)
        }

        //hide button in Non Editable mode
        if (isEditableStatus)
            holder.btnEdit_itemProfileAttachment.visibility = View.VISIBLE
        else
            holder.btnEdit_itemProfileAttachment.visibility = View.GONE

        //**** Listeners *************************************************************
        holder.itemView.setOnClickListener {
            if (model.fileId.isNotEmpty()) {
                activity.imageViewerSingleImage(model.fileId)
            }
        }

        holder.btnEdit_itemProfileAttachment.setOnClickListener {
            currentUploadPosition = position
            attachmentPickerSheetFragment.show(this)
        }

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_itemProfileAttachment = itemView.image_itemProfileAttachment
        var tvName_itemProfileAttachment = itemView.tvName_itemProfileAttachment
        var tvExtension_itemProfileAttachment = itemView.tvExtension_itemProfileAttachment
        var btnEdit_itemProfileAttachment = itemView.btnEdit_itemProfileAttachment
    }

    //***********************************************************************************************8
    //***********************************************************************************************8
    //***********************************************************************************************8

    override fun cameraResult(file: File) {
//        attachmentPickerListener.cameraResult(File())
    }

    override fun galleryResult(uri: Uri) {
        uploadAttachment(currentUploadPosition, itemsList[currentUploadPosition].type, uri)
    }

    override fun fileResult(file: File) {

    }

    private fun uploadAttachment(position: Int, attachmentType: String, uri: Uri) {

        var attachments = Utility.getFileMultipart(
            FileUtils.fileFromContentUri(
                activity,
                uri,
                //itemsList[position].type
                UUID.randomUUID().toString()
            )
        )

        activity.showProgress()
        uploadProfileAttachmentsProvider.uploadProviderProfileAttachment(
            position,
            Utility.createPartFromString(attachmentType),
            attachments
        )
    }
}