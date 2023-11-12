package com.wein_business.ui.controls.skeleton

import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.R.color
import com.ethanhua.skeleton.R.layout
import com.ethanhua.skeleton.SkeletonScreen


class RecyclerViewSkeletonScreenGrid constructor(builder: Builder) :
    SkeletonScreen {
    private val mRecyclerView: RecyclerView
    private val mActualAdapter: RecyclerView.Adapter<*>?
    private val mSkeletonAdapter: SkeletonAdapterSplitGrid
    private val mRecyclerViewFrozen: Boolean

    init {
        mRecyclerView = builder.mRecyclerView
        mActualAdapter = builder.mActualAdapter
        mSkeletonAdapter = SkeletonAdapterSplitGrid()
        mSkeletonAdapter.itemCountt = builder.mItemCount
        mSkeletonAdapter.setLayoutReference(builder.mItemResID)
        mSkeletonAdapter.shimmer(builder.mShimmer)
        mSkeletonAdapter.setShimmerColor(builder.mShimmerColor)
        mSkeletonAdapter.setShimmerAngle(builder.mShimmerAngle)
        mSkeletonAdapter.setShimmerDuration(builder.mShimmerDuration)
        mRecyclerViewFrozen = builder.mFrozen
    }

    override fun show() {
        mRecyclerView.adapter = mSkeletonAdapter
        if (!mRecyclerView.isComputingLayout && mRecyclerViewFrozen) {
            mRecyclerView.isLayoutFrozen = true
        }
    }

    override fun hide() {
        mRecyclerView.adapter = mActualAdapter
    }

    class Builder(recyclerView: RecyclerView) {
        var mActualAdapter: RecyclerView.Adapter<*>? = null
        val mRecyclerView: RecyclerView
        var mShimmer = true
        var mItemCount = 10
        var mItemResID: Int
        var mShimmerColor: Int
        var mShimmerDuration: Int
        var mShimmerAngle: Int
        var mFrozen: Boolean

        init {
            mItemResID = layout.layout_default_item_skeleton
            mShimmerDuration = 1000
            mShimmerAngle = 20
            mFrozen = true
            mRecyclerView = recyclerView
            mShimmerColor = ContextCompat.getColor(recyclerView.context, color.shimmer_color)
        }

        fun adapter(adapter: RecyclerView.Adapter<*>?): Builder {
            mActualAdapter = adapter
            return this
        }

        fun count(itemCount: Int): Builder {
            mItemCount = itemCount
            return this
        }

        fun shimmer(shimmer: Boolean): Builder {
            mShimmer = shimmer
            return this
        }

        fun duration(shimmerDuration: Int): Builder {
            mShimmerDuration = shimmerDuration
            return this
        }

        fun color(@ColorRes shimmerColor: Int): Builder {
            mShimmerColor = ContextCompat.getColor(mRecyclerView.context, shimmerColor)
            return this
        }

        fun angle(@IntRange(from = 0L, to = 30L) shimmerAngle: Int): Builder {
            mShimmerAngle = shimmerAngle
            return this
        }

        fun load(@LayoutRes skeletonLayoutResID: Int): Builder {
            mItemResID = skeletonLayoutResID
            return this
        }

        fun frozen(frozen: Boolean): Builder {
            mFrozen = frozen
            return this
        }

        fun show(): RecyclerViewSkeletonScreenGrid {
            val recyclerViewSkeleton = RecyclerViewSkeletonScreenGrid(this)
            recyclerViewSkeleton.show()
            return recyclerViewSkeleton
        }
    }
}