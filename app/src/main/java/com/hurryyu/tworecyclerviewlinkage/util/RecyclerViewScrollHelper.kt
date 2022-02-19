package com.hurryyu.tworecyclerviewlinkage.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun smoothScrollToPosition(recyclerView: RecyclerView, snapMode: Int, position: Int) {
    val layoutManager = recyclerView.layoutManager
    if (layoutManager is LinearLayoutManager) {
        val mScroller: LinearSmoothScroller = when (snapMode) {
            LinearSmoothScroller.SNAP_TO_START -> {
                TopSmoothScroller(recyclerView.context)
            }
            LinearSmoothScroller.SNAP_TO_END -> {
                BottomSmoothScroller(recyclerView.context)
            }
            else -> {
                LinearSmoothScroller(recyclerView.context)
            }
        }
        mScroller.targetPosition = position
        layoutManager.startSmoothScroll(mScroller)
    }
}

private class TopSmoothScroller constructor(context: Context) : LinearSmoothScroller(context) {
    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}

private class BottomSmoothScroller constructor(context: Context) : LinearSmoothScroller(context) {
    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_END
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_END
    }
}