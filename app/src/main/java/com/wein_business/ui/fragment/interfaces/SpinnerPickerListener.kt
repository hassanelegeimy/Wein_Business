package com.wein_business.ui.fragment.interfaces

import android.view.View
import com.wein_business.data.model.LookupModel

interface SpinnerPickerListener {
    fun onSpinnerPickerSelected(lookupSpinner: View, lookupModel: LookupModel)
}
