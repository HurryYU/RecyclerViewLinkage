package com.hurryyu.tworecyclerviewlinkage

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hurryyu.tworecyclerviewlinkage.adapter.ProductInfoAdapter
import com.hurryyu.tworecyclerviewlinkage.ext.dp
import com.hurryyu.tworecyclerviewlinkage.ext.sp
import kotlin.math.min

class ProductDecoration : RecyclerView.ItemDecoration() {
    private val headerHeight = 40F.dp

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 18F.sp
    }

    private val textBoundRect: Rect = Rect()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as ProductInfoAdapter
        val position = parent.getChildLayoutPosition(view)
        if (adapter.isGroupHeader(position)) {
            outRect.set(0, headerHeight.toInt(), 0, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val adapter = parent.adapter as ProductInfoAdapter
        val left = parent.paddingStart
        val right = parent.width - parent.paddingEnd
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val position = parent.getChildLayoutPosition(view)
            if (adapter.isGroupHeader(position)) {
                c.drawRect(
                    left.toFloat(),
                    (view.top - headerHeight),
                    right.toFloat(),
                    view.top.toFloat(),
                    paint
                )

                val groupName = adapter.getGroupName(position)
                textPaint.getTextBounds(groupName, 0, groupName.length, textBoundRect)
                val drawTextY =
                    (view.top - headerHeight / 2F) - (textBoundRect.top + textBoundRect.bottom) / 2F
                c.drawText(groupName, left.toFloat(), drawTextY, textPaint)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val left = parent.paddingStart
        val right = parent.width - parent.paddingEnd
        val top = parent.paddingTop
        val adapter = parent.adapter as ProductInfoAdapter
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager
        val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
        val itemView =
            linearLayoutManager.findViewByPosition(firstVisiblePosition) ?: return
        val nextIsGroupHeader = adapter.isGroupHeader(firstVisiblePosition + 1)
        val groupName = adapter.getGroupName(firstVisiblePosition)
        if (nextIsGroupHeader) {
            val bottom = min(itemView.bottom.toFloat(), headerHeight)
            c.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                top + bottom,
                paint
            )
            textPaint.getTextBounds(groupName, 0, groupName.length, textBoundRect)
            val drawTextY =
                (top + bottom) - headerHeight / 2F - (textBoundRect.top + textBoundRect.bottom) / 2F
            c.drawText(groupName, left.toFloat(), drawTextY, textPaint)
        } else {
            c.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                top + headerHeight,
                paint
            )

            textPaint.getTextBounds(groupName, 0, groupName.length, textBoundRect)
            val drawTextY =
                top + headerHeight / 2F - (textBoundRect.top + textBoundRect.bottom) / 2F
            c.drawText(groupName, left.toFloat(), drawTextY, textPaint)
        }
    }
}