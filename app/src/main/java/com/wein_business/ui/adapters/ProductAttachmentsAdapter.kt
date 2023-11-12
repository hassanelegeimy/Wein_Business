package com.wein_business.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wein_business.R
import com.wein_business.data.model.product.ProductAttachmentModel
import com.wein_business.ui.activity.CreateUpdateProductActivity
import com.wein_business.ui.fragment.interfaces.AttachmentPickerListener
import com.wein_business.ui.fragment.picker.AttachmentPickerSheetFragment
import com.wein_business.utils.Utility
import com.wein_business.utils.enums.ProductAttachmentEnum
import kotlinx.android.synthetic.main.item_product_attachment.view.*
import java.io.File
import kotlin.properties.Delegates

class ProductAttachmentsAdapter(
    private val activity: CreateUpdateProductActivity,
    private val itemsList: ArrayList<ProductAttachmentModel>,
    private var attachmentPickerSheetFragment: AttachmentPickerSheetFragment,
    private val isEditableStatus: Boolean
) : RecyclerView.Adapter<ProductAttachmentsAdapter.ViewHolder?>(),
    AttachmentPickerListener {

    private var currentUploadPosition by Delegates.notNull<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(activity)
            .inflate(R.layout.item_product_attachment, parent, false)

        var gridDimension = parent.measuredWidth / 3
        view.layoutParams.width = gridDimension;
        view.layoutParams.height = gridDimension;

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ProductAttachmentModel = itemsList[position]

        holder.tvName_itemProductAttachment.text = model.displayName

        if (model.fileId.isNotEmpty()) {
            Glide.with(activity)
                .load(Utility.getDownloadFileUrl(model.fileId))
                .placeholder(R.color.loading_view)
                .error(R.color.loading_view)
                .centerCrop()
                .into(holder.image_itemProductAttachment)
        } else {
            holder.image_itemProductAttachment.setImageResource(R.color.loading_view)
        }

        //hide button in Non Editable mode
        if (isEditableStatus) {
            if (model.fileId == "") {
                holder.btnAdd_itemProductAttachment.visibility = View.VISIBLE
                holder.btnDelete_itemProductAttachment.visibility = View.GONE
            } else {
                holder.btnDelete_itemProductAttachment.visibility = View.VISIBLE
                holder.btnAdd_itemProductAttachment.visibility = View.GONE
            }
        } else {
            holder.btnAdd_itemProductAttachment.visibility = View.GONE
            holder.btnDelete_itemProductAttachment.visibility = View.GONE
        }

        //**** Listeners *************************************************************
        if (model.fileId.isNotEmpty()) {
            holder.itemView.setOnClickListener {
                if (model.type == ProductAttachmentEnum.VIDEO.toString())
                    activity.activityVideoViewer(model.fileId)
                else
                    activity.imageViewerSingleImage(model.fileId)
            }
        } else {
            holder.itemView.setOnClickListener(null)
        }

        holder.btnAdd_itemProductAttachment.setOnClickListener {
            currentUploadPosition = position
            attachmentPickerSheetFragment.show(this)
        }

        holder.btnDelete_itemProductAttachment.setOnClickListener {
            activity.deleteProductAttachment(position, model)
        }

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_itemProductAttachment = itemView.image_itemProductAttachment
        var tvName_itemProductAttachment = itemView.tvName_itemProductAttachment
        var btnAdd_itemProductAttachment = itemView.btnAdd_itemProductAttachment
        var btnDelete_itemProductAttachment = itemView.btnDelete_itemProductAttachment
    }

    //*****************************************************************************
    //*****************************************************************************

    override fun cameraResult(file: File) {
//        attachmentPickerListener.cameraResult(File())
    }

    override fun galleryResult(uri: Uri) {
        //TODO check uri is not null
        activity.uploadProductAttachment(currentUploadPosition, itemsList[currentUploadPosition].type, uri)
    }

    override fun fileResult(file: File) {

    }

}