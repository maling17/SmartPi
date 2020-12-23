package com.example.smartpi.adapter.pembayaran

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.smartpi.view.pembelian.AkademikFragment
import com.example.smartpi.view.pembelian.BahasaInggrisFragment
import com.example.smartpi.view.pembelian.MengajiFragment

class PembelianPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val pages = listOf(
        AkademikFragment(),
        BahasaInggrisFragment(),
        MengajiFragment()
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
            0 -> "Matematika"
            1 -> "Bahasa Inggris"
            else -> "Mengaji"
        }
    }

}