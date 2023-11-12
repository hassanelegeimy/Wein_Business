package com.wein_business.data.providers.profile

import com.wein_business.data.api.RestClient
import com.wein_business.data.model.profile.ProfileAttachmentModel
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.providers.callback.AppCallback
import com.wein_business.data.providers.callback.AppCallbackListener
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadProfileAttachmentsProvider(var listener: Listener) {
    interface Listener {
        fun onProfileUploadAttachmentSuccess(position:Int, profileAttachmentModel: ProfileAttachmentModel)
        fun onProfileUploadAttachmentFail(message: String)
    }

    fun uploadProviderProfileAttachment(
        position:Int,
        attachmentType: RequestBody,
        attachments: MultipartBody.Part) {
        val apiCall = RestClient.getApi().uploadProviderProfileAttachment(attachmentType , attachments)
        apiCall.enqueue(AppCallback(object :
            AppCallbackListener<AppResponse<ProfileAttachmentModel>> {
            override fun onDataSuccess(body: AppResponse<ProfileAttachmentModel>) {
                listener.onProfileUploadAttachmentSuccess(position , body.data)
            }

            override fun onDataFail(Message: String) {
                listener.onProfileUploadAttachmentFail(Message)
            }
        }))
    }

}