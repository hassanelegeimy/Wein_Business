package com.wein_business.ui.adapters.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wein_business.ui.fragment.register.UserTypeFragment
import com.wein_business.ui.fragment.register.RegisterFragment
import com.wein_business.ui.fragment.register.VerifyRegisterOTPFragment

class RegisterPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val pagesCount = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserTypeFragment()
            1 -> RegisterFragment()
            2 -> VerifyRegisterOTPFragment()
            else -> UserTypeFragment()
        }
    }

    override fun getItemCount(): Int {
        return pagesCount
    }
}