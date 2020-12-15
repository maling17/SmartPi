package com.example.smartpi.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.smartpi.view.landingpage.LandingPage1Fragment
import com.example.smartpi.view.landingpage.LandingPage2Fragment
import com.example.smartpi.view.landingpage.LandingPage3Fragment

class LandingPagePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val pages = listOf(
        LandingPage1Fragment(),
        LandingPage2Fragment(),
        LandingPage3Fragment(),
    )

    override fun getCount(): Int {
        return pages.size

    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }


}