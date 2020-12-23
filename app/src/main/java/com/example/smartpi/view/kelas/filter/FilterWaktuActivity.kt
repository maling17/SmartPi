package com.example.smartpi.view.kelas.filter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.adapter.filter.FilterPagerAdapter
import com.example.smartpi.databinding.ActivityFilterWaktuBinding


class FilterWaktuActivity : AppCompatActivity() {

    lateinit var binding: ActivityFilterWaktuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFilterWaktuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBackFilter.setOnClickListener { finish() }
        binding.viewPager.adapter = FilterPagerAdapter(supportFragmentManager)
    }
}