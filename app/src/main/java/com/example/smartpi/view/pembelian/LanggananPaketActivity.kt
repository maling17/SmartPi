package com.example.smartpi.view.pembelian

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.adapter.pembayaran.PembelianPagerAdapter
import com.example.smartpi.databinding.ActivityLanggananPaketBinding

class LanggananPaketActivity : AppCompatActivity() {

    lateinit var binding: ActivityLanggananPaketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanggananPaketBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewPagerPembelian.adapter = PembelianPagerAdapter(supportFragmentManager)
        binding.ivBackLanggananPaket.setOnClickListener { finish() }
    }
}