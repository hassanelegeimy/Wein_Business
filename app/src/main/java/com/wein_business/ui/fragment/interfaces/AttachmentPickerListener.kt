package com.wein_business.ui.fragment.interfaces

import android.net.Uri
import java.io.File

interface AttachmentPickerListener {
    fun cameraResult(file: File)
    fun galleryResult(uri: Uri)
    fun fileResult(file: File)
}
