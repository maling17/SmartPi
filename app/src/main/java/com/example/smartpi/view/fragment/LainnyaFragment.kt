package com.example.smartpi.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpi.databinding.FragmentLainnyaBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.sign.SignInActivity

class LainnyaFragment : Fragment() {

    lateinit var preferences: Preferences
    private var _binding: FragmentLainnyaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLainnyaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Preferences(context!!)

        binding.btnKeluar.setOnClickListener {
            preferences.setValues("status", "0")
            val intent = Intent(context, SignInActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }


    }

}