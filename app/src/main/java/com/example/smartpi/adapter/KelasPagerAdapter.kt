package com.example.smartpi.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.smartpi.view.kelas.HistoryFragment
import com.example.smartpi.view.kelas.JadwalFragment
import com.example.smartpi.view.kelas.WaitingConfirmationFragment

class KelasPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val pages = listOf(
        JadwalFragment(),
        WaitingConfirmationFragment(),
        HistoryFragment()
    )

    override fun getCount(): Int {
        return pages.size

    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Jadwal"
            1 -> "Waiting Confirmation"
            else -> "History"
        }
    }

}