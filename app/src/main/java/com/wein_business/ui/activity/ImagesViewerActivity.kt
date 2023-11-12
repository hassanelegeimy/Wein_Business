package com.wein_business.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.adapters.pager.ImageViewerPagerAdapter
import com.wein_business.utils.Constants
import kotlinx.android.synthetic.main.activity_images_viewer.*

class ImagesViewerActivity : GenericActivity() {
    private var imagesList: ArrayList<String>? = null
    private var imagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_viewer)

        addBackPressed()

        if (intent.extras != null) {
            imagesList = intent.getStringArrayListExtra(Constants.KEY_IMAGES_LIST)
            imagePosition = intent.getIntExtra(Constants.KEY_IMAGE_POSITION, 0)
        }
        if (imagesList != null && imagesList!!.isNotEmpty()) {
            bindUI()
        } else
            finish()
    }

    private fun bindUI() {
        pager_image_viewer.adapter = ImageViewerPagerAdapter(this, imagesList!!, true)
        pager_image_viewer.currentItem = imagePosition

        indicator_image_viewer.setViewPager(pager_image_viewer)
        if (imagesList!!.size > 1) {
            indicator_image_viewer.visibility = VISIBLE
        } else {
            indicator_image_viewer.visibility = GONE
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
}