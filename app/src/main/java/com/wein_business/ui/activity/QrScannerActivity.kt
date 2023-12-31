package com.wein_business.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.wein_business.R
import com.wein_business.ui.controls.qr.ZXingBarcodeAnalyzer
import com.wein_business.utils.Constants
import kotlinx.android.synthetic.main.activity_qr_scanner.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QrScannerActivity :
    AppCompatActivity(),
    ZXingBarcodeAnalyzer.ScanningResultListener {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private var flashEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

        overlay_qr_scanner.post {
            overlay_qr_scanner.setViewFinder()
        }
    }

    override fun onScanned(result: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.QR_SCANNER_RESULT, result)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {
        if (isDestroyed || isFinishing) {
            //This check is to avoid an exception when trying to re-bind use cases but user closes the activity.
            //java.lang.IllegalArgumentException: Trying to create use case mediator with destroyed lifecycle.
            return
        }

        cameraProvider?.unbindAll()

        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(cameraPreview_qr_scanner.width, cameraPreview_qr_scanner.height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val orientationEventListener = object : OrientationEventListener(this as Context) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Int = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageAnalysis.targetRotation = rotation
            }
        }
        orientationEventListener.enable()

        var analyzer: ImageAnalysis.Analyzer = ZXingBarcodeAnalyzer(this)
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
        preview.setSurfaceProvider(cameraPreview_qr_scanner.surfaceProvider)

        val camera = cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

        if (camera?.cameraInfo?.hasFlashUnit() == true) {
            ivFlashControl_qr_scanner.visibility = View.VISIBLE
            ivFlashControl_qr_scanner.setOnClickListener {
                camera.cameraControl.enableTorch(!flashEnabled)
            }

            camera.cameraInfo.torchState.observe(this) {
                it?.let { torchState ->
                    if (torchState == TorchState.ON) {
                        flashEnabled = true
                        ivFlashControl_qr_scanner.setImageResource(R.drawable.ic_round_flash_on)
                    } else {
                        flashEnabled = false
                        ivFlashControl_qr_scanner.setImageResource(R.drawable.ic_round_flash_off)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}