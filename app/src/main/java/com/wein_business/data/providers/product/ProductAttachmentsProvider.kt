package com.wein_business.data.providers.product

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.product.ProductAttachmentModel
import com.wein_business.data.model.request.ProductAttachmentDeleteRequest
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.response.NoObjectResponse
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductAttachmentsProvider(var listener: Listener) {
    interface Listener {
        fun onProductUploadAttachmentSuccess(position:Int, productAttachmentModel: ProductAttachmentModel)
        fun onProductUploadAttachmentFail(message: String)
        fun onProductDeleteAttachmentSuccess(position:Int)
        fun onProductDeleteAttachmentFail(message: String)
    }

    fun uploadProductAttachment(
        position:Int ,
        activityId: RequestBody,
        attachmentCategory: RequestBody,
        attachments: MultipartBody.Part
    ) {
        val apiCall = RestClient.getApi().uploadProductAttachment(activityId , attachmentCategory , attachments)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<ProductAttachmentModel>> {
            override fun onDataSuccess(body: AppResponse<ProductAttachmentModel>) {
                listener.onProductUploadAttachmentSuccess(position , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onProductUploadAttachmentFail(Message)
            }
        }))
    }

    fun deleteProductAttachment(position:Int , request: ProductAttachmentDeleteRequest) {
        val apiCall = RestClient.getApi().deleteProductAttachment(request)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<NoObjectResponse>> {
            override fun onDataSuccess(body: AppResponse<NoObjectResponse>) {
                listener.onProductDeleteAttachmentSuccess(position)
            }

            override fun onDataFail(Message: String) {
                listener.onProductDeleteAttachmentFail(Message)
            }
        }))
    }
}