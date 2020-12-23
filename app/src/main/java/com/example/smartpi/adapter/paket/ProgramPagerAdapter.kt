package com.example.smartpi.adapter.paket

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.smartpi.view.program.CalistungTkFragment
import com.example.smartpi.view.program.SDFragment
import com.example.smartpi.view.program.SMPFragment

class ProgramPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val pages = listOf(
        CalistungTkFragment(),
        SDFragment(),
        SMPFragment()
    )

    override fun getCount(): Int {
        return pages.size

    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "TK"
            1 -> "SD"
            else -> "SMP"
        }
    }

}