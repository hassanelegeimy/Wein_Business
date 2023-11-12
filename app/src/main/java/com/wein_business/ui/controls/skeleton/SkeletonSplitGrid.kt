package com.wein_business.ui.controls.skeleton

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.ViewSkeletonScreen

object SkeletonSplitGrid {
    fun bind(recyclerView: RecyclerView): RecyclerViewSkeletonScreenGrid.Builder {
        return RecyclerViewSkeletonScreenGrid.Builder(recyclerView)
    }

    fun bind(view: View?): ViewSkeletonScreen.Builder {
        return ViewSkeletonScreen.Builder(view)
    }
}
