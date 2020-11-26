package com.example.smartpi.adapter.VAHolder

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.smartpi.R


class VirtualAccountAdapter(
    private var context: Context? = null,
    private var expandableListTitle: List<String>? = null,
    private var expandableListDetail: HashMap<String, List<String>>? = null

) : BaseExpandableListAdapter() {

    fun VirtualAccountAdapter(
        context: Context?, expandableListTitle: List<String>?,
        expandableListDetail: HashMap<String, List<String>>?
    ) {
        this.context = context
        this.expandableListTitle = expandableListTitle
        this.expandableListDetail = expandableListDetail
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any? {
        return expandableListDetail!![expandableListTitle!![listPosition]]
            ?.get(expandedListPosition)
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int, expandedListPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String?
        if (convertView == null) {
            val layoutInflater = context
                ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.panduan_expanded, null)
        }
        val expandedListTextView = convertView
            ?.findViewById<View>(R.id.tv_desc_panduan_expanded) as TextView
        expandedListTextView.text = expandedListText
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return expandableListDetail!![expandableListTitle!![listPosition]]
            ?.size!!
    }

    override fun getGroup(listPosition: Int): Any {
        return expandableListTitle!![listPosition]
    }

    override fun getGroupCount(): Int {
        return expandableListTitle!!.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String?
        if (convertView == null) {
            val layoutInflater =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.panduan_collapsed, null)
        }
        val listTitleTextView = convertView
            ?.findViewById<View>(R.id.tv_title_panduan_collapse) as TextView
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}