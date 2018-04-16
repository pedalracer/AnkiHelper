package com.example.antonrooseleer.ankihelper.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import com.example.antonrooseleer.ankihelper.R

class SimpleDividerItemDecorator (context: Context) : RecyclerView.ItemDecoration() {
    private var mDivider: Drawable

    init {
        mDivider = context.getResources().getDrawable(R.drawable.divider)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.getPaddingLeft()
        val right = parent.getWidth() - parent.getPaddingRight()
        val childCount = parent.getChildCount()

        for (i in 0..childCount - 1) {

            var child = parent.getChildAt(i)

            var params = child.getLayoutParams() as (RecyclerView.LayoutParams)

            val top = child.getBottom() + params.bottomMargin
            val bottom = top + mDivider.getIntrinsicHeight()

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}