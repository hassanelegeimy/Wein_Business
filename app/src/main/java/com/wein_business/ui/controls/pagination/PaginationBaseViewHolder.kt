package com.wein_business.ui.controls.pagination

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationBaseViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private var currentPosition :Int = 0

    open fun onBind(position: Int) {
        currentPosition = position
    }
}