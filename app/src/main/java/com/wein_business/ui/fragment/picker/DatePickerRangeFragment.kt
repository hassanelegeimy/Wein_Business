package com.wein_business.ui.fragment.picker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.wein_business.utils.LocaleHelper2
import com.wein_business.utils.Utility.convertToDateFormat
import com.wein_business.utils.Utility.isValidDateRange
import java.util.*

class DatePickerRangeFragment : DialogFragment(), OnDateSetListener {
    private var targetEditText: EditText? = null
    private var relatedEditText: EditText? = null
    private var todayIsMin = false
    private var todayIsMaximum = false
    private var isStartDate = false
    private var minimumDateCalendar: Calendar? = null
    private var datePickerDialog: DatePickerDialog? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        LocaleHelper2.onAttach(requireActivity())
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        if (todayIsMin) {
            val calendar = Calendar.getInstance()
            datePickerDialog!!.datePicker.minDate = calendar.time.time
        }
        if (todayIsMaximum) {
            val today = Calendar.getInstance()
            datePickerDialog!!.datePicker.maxDate = today.time.time
        }
        if (minimumDateCalendar != null) {
            datePickerDialog!!.datePicker.minDate = minimumDateCalendar!!.time.time
        }
        return datePickerDialog!!
    }

    fun setMinMax(todayIsMin: Boolean, todayIsMaximum: Boolean, isStartDate: Boolean) {
        this.todayIsMin = todayIsMin
        this.todayIsMaximum = todayIsMaximum
        this.isStartDate = isStartDate
    }

    fun setMinimumDateCalendar(minimumDateCalendar: Calendar?) {
        this.minimumDateCalendar = minimumDateCalendar
    }

    fun setTargetEditText(targetEditText: EditText?, relatedEditText: EditText?) {
        this.targetEditText = targetEditText
        this.relatedEditText = relatedEditText
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val currentDateString = convertToDateFormat(year, month + 1, dayOfMonth)
        targetEditText!!.setText(currentDateString)
        if (isStartDate && !isValidDateRange(targetEditText!!, relatedEditText!!)) {
            relatedEditText!!.setText(currentDateString)
        } else if (!isStartDate && !isValidDateRange(relatedEditText!!, targetEditText!!)) {
            relatedEditText!!.setText(currentDateString)
        }
    }
}