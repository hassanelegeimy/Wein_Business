package com.wein_business.ui.fragment.generic

import androidx.fragment.app.Fragment
import com.wein_business.utils.LocaleHelper2

open class GenericFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        LocaleHelper2.onAttach(requireActivity())
    }

}