package com.example.smartpi.view.pembelian

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.R
import com.example.smartpi.adapter.PembelianPagerAdapter
import kotlinx.android.synthetic.main.activity_langganan_paket.*

class LanggananPaketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_langganan_paket)

        viewPagerPembelian.adapter = PembelianPagerAdapter(supportFragmentManager)

    }
}