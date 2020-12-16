package com.example.smartpi.view.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartpi.databinding.FragmentLainnyaBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.lain.SyaratDanLainActivity
import com.example.smartpi.view.lain.UbahKataSandiActivity
import com.example.smartpi.view.lain.UbahProfilActivity
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
        preferences = Preferences(requireContext())



        binding.btnKeluar.setOnClickListener {
            preferences.setValues("status", "0")
            val intent = Intent(context, SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.ivUbahProfile.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    UbahProfilActivity::class.java
                )
            )
        }
        binding.ivUbahPassword.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    UbahKataSandiActivity::class.java
                )
            )
        }

        binding.ivBantuan.setOnClickListener {

            val intent = Intent(activity, SyaratDanLainActivity::class.java)
            intent.putExtra("kategori", "bantuan")
            startActivity(intent)

        }


        binding.ivTentangKami.setOnClickListener {

            val intent = Intent(activity, SyaratDanLainActivity::class.java)
            intent.putExtra("kategori", "tentang_kami")
            startActivity(intent)

        }

        binding.ivKebijakan.setOnClickListener {

            val intent = Intent(activity, SyaratDanLainActivity::class.java)
            intent.putExtra("kategori", "kebijakan")
            startActivity(intent)

        }

        binding.ivSyarat.setOnClickListener {

            val intent = Intent(activity, SyaratDanLainActivity::class.java)
            intent.putExtra("kategori", "syarat")
            startActivity(intent)

        }
        binding.ivHubungi.setOnClickListener {
            sendToWhatsapp()
        }

    }

    private fun sendToWhatsapp() {
        val contact = "+62 81211718296"
        val message = "Hallo Smartpi. Saya butuh bantuan"
        val url = "https://api.whatsapp.com/send?phone=$contact&text=$message"

        try {
            val packageManager = requireContext().packageManager
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val intent = Intent(Intent.ACTION_VIEW)

            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(context, "Check Your Connection", Toast.LENGTH_LONG).show()
        }
    }
}