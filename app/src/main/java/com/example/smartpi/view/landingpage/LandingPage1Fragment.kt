package com.example.smartpi.view.landingpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpi.R
import com.example.smartpi.databinding.FragmentLandingPage1Binding

class LandingPage1Fragment : Fragment() {

    var _binding: FragmentLandingPage1Binding? = null
    val binding get() = _binding!!
    val landing2 = LandingPage2Fragment()
    val landing3 = LandingPage3Fragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLandingPage1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setFragment(fragment: Fragment) {

        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_1, fragment)
        fragmentTransaction.commit()
    }
}