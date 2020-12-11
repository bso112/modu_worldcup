package com.manta.worldcup.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val mSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        outRect.left = mSpace;
//        outRect.right = mSpace;
//        outRect.bottom = mSpace;
        outRect.top = mSpace

    }
}