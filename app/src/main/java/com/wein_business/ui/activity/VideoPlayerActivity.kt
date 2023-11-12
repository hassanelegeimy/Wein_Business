package com.wein_business.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.google.android.exoplayer2.SimpleExoPlayer
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Constants
import com.wein_business.utils.LocaleHelper2
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.activity_video_player.*


class VideoPlayerActivity : GenericActivity() {
    //    private var exoPlayer: ExoPlayer? = null
    private var simpleExoPlayer: SimpleExoPlayer? = null

    private var fileId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        addBackPressed()

        if (intent.extras != null) {
            fileId = intent.getStringExtra(Constants.KEY_VIDEO_URL)
        }

        //check if valid URL
        if (fileId != null && fileId!!.isNotEmpty())
            bindUI()
        else
            finish()
    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer?.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer?.release()
    }

    fun bindUI() {
        simpleExoPlayer = Utility.setUpVideoPlayer(
            this,
            video_player_fullscreen,
            fileId!! ,
            autoPlay = true
        )
    }
    //**************************************************************
    //******Listeners***********************************************

    private fun addBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                animateScreenTransition()
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper2.onAttach(this)
    }

}