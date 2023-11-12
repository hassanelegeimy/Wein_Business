package com.wein_business.ui.fragment.generic

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.wein_business.R
import com.wein_business.utils.LocaleHelper2
import com.wein_business.utils.NetworkUtils
import com.wein_business.utils.Utility

abstract class DataFragment : GenericFragment() {

    private var layout_data_error: LinearLayout? = null
    private var TV_dataError: TextView? = null
    private var image_dataError: ImageView? = null

    override fun onResume() {
        super.onResume()
        LocaleHelper2.onAttach(requireActivity())
    }

    fun bind(view: View) {
        layout_data_error = view.findViewById(R.id.layout_data_error)
        TV_dataError = view.findViewById(R.id.TV_dataError)
        image_dataError = view.findViewById(R.id.image_dataError)

        image_dataError!!.setOnClickListener { loadData() }
    }

    fun isConnected(): Boolean {
        return if (NetworkUtils.isConnected()) {
            layout_data_error!!.visibility = View.GONE
            true
        } else {
            noConnection()
            false
        }
    }

    private fun noConnection() {
        layout_data_error!!.visibility = View.VISIBLE
        TV_dataError!!.text = Utility.getString(R.string.no_connection)
        image_dataError!!.setImageResource(R.drawable.ic_no_connection)
    }

    fun noData() {
        layout_data_error!!.visibility = View.VISIBLE
        TV_dataError!!.text = Utility.getString(R.string.no_data)
        image_dataError!!.setImageResource(R.drawable.ic_no_data)
    }

    fun errorData(errorMessage: String?) {
        layout_data_error!!.visibility = View.VISIBLE
        if (errorMessage != null) TV_dataError!!.text = errorMessage else TV_dataError!!.text =
            Utility.getString(R.string.error_api_msg)
        image_dataError!!.setImageResource(R.drawable.ic_no_data)
    }

    protected abstract fun loadData()
}