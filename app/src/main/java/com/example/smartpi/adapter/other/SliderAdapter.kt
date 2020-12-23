package com.example.smartpi.adapter.other

import android.content.Context
import android.view.View
import androidx.viewpager.widget.PagerAdapter


class SliderAdapter : PagerAdapter() {
    private var context: Context? = null
    private var reminder: List<String>? = null

    fun SliderAdapter(context: Context?, reminder: List<String>?) {
        this.context = context
        this.reminder = reminder
    }

    override fun getCount(): Int = 2

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}