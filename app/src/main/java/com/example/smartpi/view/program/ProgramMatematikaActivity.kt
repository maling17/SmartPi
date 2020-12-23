package com.example.smartpi.view.program

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.adapter.paket.ProgramPagerAdapter
import com.example.smartpi.databinding.ActivityProgramMatematikaBinding

class ProgramMatematikaActivity : AppCompatActivity() {
    lateinit var binding: ActivityProgramMatematikaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgramMatematikaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewPager.adapter = ProgramPagerAdapter(supportFragmentManager)
        binding.ivBackProgramMatematika.setOnClickListener { finish() }

    }
}