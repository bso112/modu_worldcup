package com.manta.worldcup.helper

import android.view.ViewGroup
import android.widget.TextView



object MyMenuInflater {

    fun AddMenuToViewGrop(parent: ViewGroup, menu: List<Pair<String, ()->Unit>>) {
        for (item in menu) {
            val tv = TextView(parent.context)
            tv.text = item.first
            tv.setOnClickListener {
                item.second();
            }
            parent.addView(tv);
        }
    }
}