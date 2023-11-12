package com.wein_business.ui.fragment.picker

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wein_business.R
import com.wein_business.data.model.LookupModel
import com.wein_business.ui.fragment.interfaces.SpinnerPickerListener
import com.wein_business.ui.activity.generic.GenericActivity
import com.wein_business.utils.Utility
import kotlinx.android.synthetic.main.spinner_picker_dialog.*

class SpinnerPickerDialogFragment : BottomSheetDialogFragment() {
    private lateinit var activity: GenericActivity
    private lateinit var spinnerPickerListener: SpinnerPickerListener
    private lateinit var lookupList: ArrayList<LookupModel>
    private lateinit var lookupName: String
    private lateinit var lookupSpinner: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    fun show(
        activity: GenericActivity,
        spinnerPickerListener: SpinnerPickerListener,
        lookupList: ArrayList<LookupModel>,
        lookupName: String,
        lookupSpinner: View
    ) {
        show(activity.supportFragmentManager, SpinnerPickerDialogFragment::class.java.simpleName)
        this.activity = activity
        this.spinnerPickerListener = spinnerPickerListener
        this.lookupList = lookupList
        this.lookupName = lookupName
        this.lookupSpinner = lookupSpinner
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.spinner_picker_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    fun bindUI() {
        tvTitle_spinner_picker.text = Utility.getString(R.string.choose) + lookupName

        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, lookupList)
        listview_spinner_picker.adapter = adapter
        listview_spinner_picker.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                spinnerPickerListener.onSpinnerPickerSelected(lookupSpinner , lookupList[position])
                dismiss()
            }
    }
}