package com.wein_business.ui.activity

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.wein_business.R
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.ui.adapters.OnboardingPagerAdapter
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        initViews()
        initListeners()
    }

    private fun initViews() {
        val onboardingTitles = resources.getStringArray(R.array.onboarding_titles).toList()
        val onboardingDescriptions =
            resources.getStringArray(R.array.onboarding_descriptions).toList()
        val onboardingImages =
            arrayListOf(R.drawable.onboard_1, R.drawable.onboard_2, R.drawable.onboard_3)

        pager_onboarding.adapter = OnboardingPagerAdapter(
            onboardingTitles,
            onboardingDescriptions,
            onboardingImages
        )

        indicator_onboarding.setViewPager(pager_onboarding)
    }

    private fun initListeners() {

        pager_onboarding.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    btn_onboarding_next.text = getString(R.string.start)
                } else {
                    btn_onboarding_next.text = getString(R.string.next)
                }
            }
        })
    }

    fun onClick(view: View) {
        when (view) {
            btn_onboarding_next -> onboardingNext()
        }
    }

    private fun onboardingNext() {
        if (pager_onboarding.currentItem < 2)//0 or 1
            pager_onboarding.currentItem = pager_onboarding.currentItem + 1
        else
            activityLogin(false)
    }
}