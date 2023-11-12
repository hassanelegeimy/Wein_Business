package com.wein_business.data.api

import com.wein_business.data.model.ProviderStatusModel
import com.wein_business.data.model.authorize.OtpModel
import com.wein_business.data.model.authorize.UserModel
import com.wein_business.data.model.product.ProductAttachmentModel
import com.wein_business.data.model.product.ProviderProductModel
import com.wein_business.data.model.profile.CompanyProfileModel
import com.wein_business.data.model.profile.IndividualProfileModel
import com.wein_business.data.model.profile.ProfileAttachmentModel
import com.wein_business.data.model.request.*
import com.wein_business.data.model.request.authorize.*
import com.wein_business.data.model.response.AppResponse
import com.wein_business.data.model.response.AppResponseList
import com.wein_business.data.model.response.NoObjectResponse
import com.wein_business.data.model.ticket.TicketSearchModel
import com.wein_business.data.model.trip.ProviderTripModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    //**** Public ****************************************************************
    //**************************************************************************

//    @GET("public/activityCategories")
//    fun getMainCategories(): Call<AppResponseList<CategoryModel>>

    //**** Registration*********************************************************
    //**************************************************************************
    @POST("public/registration/register")
    fun register(@Body registerRequest: RegisterRequest): Call<AppResponse<OtpModel>>

    @POST("public/registration/resendOtp")
    fun resendOtp(@Body resendOTPRequest: ResendOtpRequest): Call<AppResponse<OtpModel>>

    @POST("public/registration/verifyRegisterOTP")
    fun verifyRegisterOTP(@Body verifyOTPRequest: VerifyOtpRequest): Call<AppResponse<UserModel>>

    //**** Login****************************************************************
    //**************************************************************************
    @POST("public/login")
    fun login(@Body loginRequest: LoginRequest): Call<AppResponse<UserModel>>

    @POST("public/refreshToken")
    fun renewToken(@Body renewTokenRequest: RenewTokenRequest): Call<AppResponse<UserModel>>

    //*******
    //**** Profile & Account ****************************************************************
    //**************************************************************************
    @GET("provider-company/getProfile")
    fun getCompanyProfile(): Call<AppResponse<CompanyProfileModel>>

    @POST("provider-company/updateProfile")
    fun updateCompanyProfile(@Body body: UpdateProfileCompanyRequest): Call<AppResponse<CompanyProfileModel>>

    @POST("provider-company/submitProfile")
    fun submitCompanyProfile(@Body body: UpdateProfileCompanyRequest): Call<AppResponse<CompanyProfileModel>>

    @GET("provider-user/getProfile")
    fun getIndividualProfile(): Call<AppResponse<IndividualProfileModel>>

    @POST("provider-user/updateProfile")
    fun updateIndividualProfile(@Body body: UpdateProfileIndividualRequest): Call<AppResponse<IndividualProfileModel>>

    @POST("provider-user/submitProfile")
    fun submitIndividualProfile(@Body body: UpdateProfileIndividualRequest): Call<AppResponse<IndividualProfileModel>>

    @Multipart
    @POST("provider/uploadProviderAttachment")
    fun uploadProviderProfileAttachment(
        @Part("attachmentType") attachmentType: RequestBody,
        @Part attachments: MultipartBody.Part
    ): Call<AppResponse<ProfileAttachmentModel>>

    @GET("provider/getProviderStatus")
    fun getProviderStatus(): Call<AppResponse<ProviderStatusModel>>
    //****Provider Products ****************************************************
    //**************************************************************************

    @GET("provider/getAllActivities")
    fun getProviderProducts(): Call<AppResponseList<ProviderProductModel>>

    @GET("provider/getActivityDetails/{activityId}")
    fun getProviderProductDetails(@Path("activityId") activityId: Int): Call<AppResponse<ProviderProductModel>>

    @POST("provider/createUpdateActivity")
    fun createUpdateProduct(@Body body: CreateUpdateProductRequest): Call<AppResponse<ProviderProductModel>>

    @POST("provider/submitActivity")
    fun submitProduct(@Body body: CreateUpdateProductRequest): Call<AppResponse<ProviderProductModel>>

    @Multipart
    @POST("provider/uploadActivityAttachment")
    fun uploadProductAttachment(
        @Part("activityId") activityId: RequestBody,
        @Part("attachmentCategory") attachmentCategory: RequestBody,
        @Part attachments: MultipartBody.Part
    ): Call<AppResponse<ProductAttachmentModel>>

    @POST("provider/deleteActivityAttachment")
    fun deleteProductAttachment(@Body body: ProductAttachmentDeleteRequest): Call<AppResponse<NoObjectResponse>>

    //**** Trips ****************************************************************
    //**************************************************************************
    @GET("provider/getProviderUpcomingTrips")
    fun getProviderUpcomingTrips(): Call<AppResponseList<ProviderTripModel>>

    @GET("provider/getActivityTrips/{activityId}")
    fun getActivityTrips(@Path("activityId") activityId: Int): Call<AppResponseList<ProviderTripModel>>

    @POST("provider/createTrip")
    fun createTrip(@Body request: AddTripRequest): Call<AppResponse<NoObjectResponse>>

    @GET("provider/getTicketDetails")
    fun searchTicket(@Header("barcodeBooking") barcodeBooking: String): Call<AppResponse<TicketSearchModel>>
}