package com.example.smartpi

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartpi.databinding.ActivityMainBinding
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.fragment.HomeFragment
import com.example.smartpi.view.fragment.KelasFragment
import com.example.smartpi.view.fragment.LainnyaFragment
import com.example.smartpi.view.fragment.PembelianFragment

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val kelasFragment = KelasFragment()
    private val pembelianFragment = PembelianFragment()
    private val lainnyaFragment = LainnyaFragment()
    lateinit var preferences: Preferences
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preferences = Preferences(this)
        setFragment(homeFragment)

        binding.btvHome.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_navigation -> {
                    setFragment(homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.kelas_navigation -> {
                    setFragment(kelasFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.bantuan_navigation -> {
                    sendToWhatsapp()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.pembelian_navigation -> {
                    setFragment(pembelianFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.lain_navigation -> {
                    setFragment(lainnyaFragment)
                    return@setOnNavigationItemSelectedListener true

                }

                else -> false
            }
        }


    }

    private fun setFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment)
        fragmentTransaction.commit()
    }

    private fun sendToWhatsapp() {
        val contact = "+62 81211718296"
        val message = "Hallo Smartpi. Saya butuh bantuan"
        val url = "https://api.whatsapp.com/send?phone=$contact&text=$message"

        try {
            val packageManager = applicationContext.packageManager
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val intent = Intent(Intent.ACTION_VIEW)

            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, "Check Your Connection", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            "Tekan tombol KEMBALI lagi untuk keluar dari aplikasi",
            Toast.LENGTH_SHORT
        ).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

    }
}