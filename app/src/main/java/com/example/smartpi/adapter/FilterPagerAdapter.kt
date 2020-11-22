package com.example.smartpi.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.smartpi.view.kelas.filter.WaktuKhususFragment
import com.example.smartpi.view.kelas.filter.WaktuUmumFragment

class FilterPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val pages = listOf(
        WaktuUmumFragment(),
        WaktuKhususFragment(),
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
            0 -> "Waktu Umum"
            else -> "Waktu Khusus"

        }
    }

}