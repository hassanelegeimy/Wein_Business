package com.wein_business.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.zxing.WriterException
import com.wein_business.BaseApp
import com.wein_business.R
import com.wein_business.data.model.ProductStatusModel
import com.wein_business.data.model.ProviderStatusModel
import com.wein_business.data.model.product.ProductAttachmentModel
import com.wein_business.utils.enums.ProductAttachmentEnum
import com.wein_business.utils.enums.ProductStatusEnum
import com.wein_business.utils.enums.ProviderStatusEnum
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object Utility {

    fun getString(id: Int): String {
        return BaseApp.appContext.resources.getString(id)
    }

    fun exitApp(activity: Activity) {
        try {
            val am = BaseApp.appContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appTasks = am.appTasks
            if (appTasks.size > 0) {
                val appTask = appTasks[0]
                appTask.finishAndRemoveTask()
            }
        } catch (ex: Exception) {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(startMain)
        }
    }

    //*************************************************************
    //** URLs ***************************************************
    fun getDownloadFileUrl(fileId: String): String {
        return Constants.downloadFileUrl + "?fileId=" + fileId
    }

    fun openExternalUrl(activity: Activity, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(browserIntent)
    }

    //*************************************************************
    //*************************************************************
    //*** Main Data Management *************************************

    fun isProviderStatusAllowEditMode(status: ProviderStatusModel): Boolean {
        //TODO DEFINE ACCURATE EDIT MODES FROM BUSSINESS
        return !(status.userStatusEnum == ProviderStatusEnum.PENDING_APPROVAL.toString() ||
                status.userStatusEnum == ProviderStatusEnum.REJECTED.toString() ||
                status.userStatusEnum == ProviderStatusEnum.SUSPENDED.toString() ||
                status.userStatusEnum == ProviderStatusEnum.ACTIVE.toString())
    }

    fun isProductStatusAllowEditMode(status: ProductStatusModel): Boolean {
        //TODO DEFINE ACCURATE EDIT MODES FROM BUSSINESS
        return !(status.statusEnum == ProductStatusEnum.PENDING_APPROVAL.toString() ||
                status.statusEnum == ProductStatusEnum.REJECTED.toString() ||
                status.statusEnum == ProductStatusEnum.SUSPENDED.toString() ||
                status.statusEnum == ProductStatusEnum.ACTIVE.toString())
    }

    //****************************************************************
    //****************************************************************
    //*****Design********************************************************

    fun setUpVideoPlayer(context: Context, playerView: PlayerView, fileId: String, autoPlay: Boolean): SimpleExoPlayer? {
//        exoPlayer = ExoPlayer.Builder(this).build()
        var simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
        playerView.player = simpleExoPlayer
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Weeein"))
        val mediaSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(getDownloadFileUrl(fileId)))
        simpleExoPlayer?.prepare(mediaSource)
        if (autoPlay)
            simpleExoPlayer?.playWhenReady = true
        return simpleExoPlayer
    }

    fun setSwipeToRefreshColor(swipeRefreshLayout: SwipeRefreshLayout, context: Context) {
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                context, R.color.colorPrimary
            )
        )
    }

    fun addRecyclerViewDivider(context: Context, recyclerView: RecyclerView) {
        val dividerItemDecoration = DividerItemDecoration(
            context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    fun showQRImage(qrText: String, imageView: ImageView) {
        val qrgEncoder = QRGEncoder(qrText, null, QRGContents.Type.TEXT,60 )
        qrgEncoder.colorBlack = Color.BLACK
        qrgEncoder.colorWhite = Color.WHITE
        try {
            imageView.setImageBitmap(qrgEncoder.bitmap)
        } catch (e: WriterException) {
            //TODO SHOW QR PLACEJOLDER
           e.printStackTrace()
        }
    }
    //*************************************************************
    //*************************************************************
    //***Files And Attachments*************************************

    fun createPartFromString(descriptionString: String): RequestBody {
        return descriptionString
            .toRequestBody(MultipartBody.FORM)
    }

    fun getFileMultipart(file: File): MultipartBody.Part {

        return MultipartBody.Part
            .createFormData(
                name = "attachments",
                filename = file.name,
                body = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
    }

    //*****************************************************************
    // PRODUCT ATTACHMENTS TYPES ENUM
    fun getProductVideoObject(fileId: String): ProductAttachmentModel {
        return ProductAttachmentModel(fileId, ProductAttachmentEnum.VIDEO.toString(), getString(R.string.attachment_video))
    }

    fun getProductImageMasterObject(fileId: String): ProductAttachmentModel {
        return ProductAttachmentModel(fileId, ProductAttachmentEnum.IMAGE_MASTER.toString(), getString(R.string.attachment_image_master))
    }

    fun getProductImageObject(fileId: String): ProductAttachmentModel {
        return ProductAttachmentModel(fileId, ProductAttachmentEnum.IMAGE.toString(), getString(R.string.attachment_image))
    }

    //*************************************************************
    //*General Utils **********************************************
    fun getDeviceMetrics(context: Context): DisplayMetrics? {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

    fun convertToDateFormat(
        year: Int,
        monthIndex: Int,
        dayOfMonth: Int
    ): String {
        return String.format(
            Locale.ENGLISH,
            "%02d-%02d",
            dayOfMonth,
            monthIndex
        ) + "-$year"
    }

    fun isValidDateRange(startDate: EditText, endDate: EditText): Boolean {
        if (startDate.text.toString().isEmpty() || endDate.text.toString().isEmpty()) return false
        val startDateObj: Date = getDateSimpleFormat(startDate.text.toString())
        val endDateObj: Date = getDateSimpleFormat(endDate.text.toString())
        return endDateObj.time >= startDateObj.time
    }

    private fun getDateSimpleFormat(dateStr: String?): Date {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy")

        return inputFormat.parse(dateStr)
    }
}