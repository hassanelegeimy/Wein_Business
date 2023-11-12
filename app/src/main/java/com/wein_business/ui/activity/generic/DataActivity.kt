package com.wein_business.ui.activity.generic

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.wein_business.utils.*
import com.wein_business.R

abstract class DataActivity : GenericActivity() {

    private var layout_data_error: LinearLayout? = null
    private var TV_dataError: TextView? = null
    private var image_dataError: ImageView? = null


    fun bindListPlaceholder(){
        layout_data_error = findViewById(R.id.layout_data_error)
        TV_dataError = findViewById(R.id.TV_dataError)
        image_dataError = findViewById(R.id.image_dataError)


        image_dataError!!.setOnClickListener {
            loadData()
        }
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