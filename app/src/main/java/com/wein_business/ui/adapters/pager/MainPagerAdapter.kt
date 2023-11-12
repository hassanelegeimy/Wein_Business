package com.wein_business.ui.adapters.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wein_business.ui.fragment.main.*

class MainPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var positionProducts = 0
    var positionTRIPS = 1
    var positionACCOUNT = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProviderProductsFragment()
            1 -> ProviderUpcomingTripsFragment()
            2 -> AccountProviderFragment()
            else -> ProviderProductsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}