package com.wein_business.ui.controls.skeleton

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.R
import io.supercharge.shimmerlayout.ShimmerLayout

class SkeletonAdapterSplitGrid : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var itemCountt = 0
    private var mLayoutReference = 0
    private var mColor = 0
    private var mShimmer = false
    private var mShimmerDuration = 0
    private var mShimmerAngle = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (mShimmer) {
            ShimmerViewHolderGrid(inflater, parent, mLayoutReference)
        } else object : RecyclerView.ViewHolder(inflater.inflate(mLayoutReference, parent, false)) {}
    }

    override fun getItemCount(): Int {
        return  itemCountt
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mShimmer) {
            val layout = holder.itemView as ShimmerLayout
            layout.setShimmerAnimationDuration(mShimmerDuration)
            layout.setShimmerAngle(mShimmerAngle)
            layout.setShimmerColor(mColor)
            layout.startShimmerAnimation()
        }
    }

    fun setLayoutReference(layoutReference: Int) {
        mLayoutReference = layoutReference
    }

    fun setShimmerColor(color: Int) {
        mColor = color
    }

    fun shimmer(shimmer: Boolean) {
        mShimmer = shimmer
    }

    fun setShimmerDuration(shimmerDuration: Int) {
        mShimmerDuration = shimmerDuration
    }

    fun setShimmerAngle(shimmerAngle: Int) {
        mShimmerAngle = shimmerAngle
    }

    class ShimmerViewHolderGrid(inflater: LayoutInflater, parent: ViewGroup, innerViewResId: Int) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_shimmer, parent, false)) {
        init {
            val layout = itemView as ViewGroup
            val view = inflater.inflate(innerViewResId, layout, false)
            val lp = view.layoutParams
            if (lp != null) {
                layout.layoutParams = lp
            }
            var gridDimension = parent.measuredWidth / 2
            view.layoutParams.width = gridDimension;
            view.layoutParams.height = gridDimension;

            layout.addView(view)
        }
    }

}
