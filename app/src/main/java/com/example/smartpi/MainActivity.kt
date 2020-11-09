package com.example.smartpi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartpi.fragment.HomeFragment
import com.example.smartpi.fragment.KelasFragment
import com.example.smartpi.utils.Preferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val homeFragment = HomeFragment()
    val kelasFragment = KelasFragment()
    lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = Preferences(this)


        setFragment(homeFragment)

        btv_home.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_navigation -> {
                    setFragment(homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.kelas_navigation->{
                    setFragment(kelasFragment)
                    return@setOnNavigationItemSelectedListener  true
                }
                R.id.lain_navigation ->{
                    preferences.setValues("status","0")
                    finish()
                    return@setOnNavigationItemSelectedListener  true

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
}